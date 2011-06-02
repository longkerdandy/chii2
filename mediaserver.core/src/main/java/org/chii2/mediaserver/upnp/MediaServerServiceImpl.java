package org.chii2.mediaserver.upnp;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.api.provider.OnlineVideoProviderService;
import org.chii2.mediaserver.api.upnp.MediaServerService;
import org.chii2.transcoder.api.core.TranscoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.DefaultUpnpServiceConfiguration;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.binding.LocalServiceBindingException;
import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.profile.HeaderDeviceDetailsProvider;
import org.teleal.cling.model.types.*;
import org.teleal.cling.support.connectionmanager.ConnectionManagerService;
import org.teleal.cling.transport.impl.apache.StreamClientConfigurationImpl;
import org.teleal.cling.transport.impl.apache.StreamClientImpl;
import org.teleal.cling.transport.spi.StreamClient;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UPnP/DLNA Media Server
 */
public class MediaServerServiceImpl implements MediaServerService {

    // UDN
    private final UDN udn = UDN.uniqueSystemIdentifier("Chii2 MediaServer v1");
    // Server Prefix
    private String serverPrefix = "Home Server";
    // UPnP Server
    private UpnpService upnpService = null;
    // Library
    private MediaLibraryService mediaLibrary;
    // HTTP Server
    private HttpServerService httpService;
    // Transcoder
    private TranscoderService transcoder;
    // Online Videos
    private List<OnlineVideoProviderService> onlineVideos;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.upnp");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Server MediaServerService (Core) init.");

        // Set Server Prefix to Host Name
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            if (StringUtils.isNotBlank(hostName)) {
                serverPrefix = hostName;
            }
        } catch (UnknownHostException ignore) {
        }

        try {
            // Init UPnP stack
            upnpService = new UpnpServiceImpl(new DefaultUpnpServiceConfiguration(8895) {
                @Override
                public StreamClient<StreamClientConfigurationImpl> createStreamClient() {
                    return new StreamClientImpl(new StreamClientConfigurationImpl());
                }

            });
            // Attach service and device
            upnpService.getRegistry().addDevice(createUPnPDevice());
        } catch (Exception e) {
            logger.error("Chii MediaServerService init with exception: {}.", e.getMessage());
        }
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Server MediaServerService (Core) destroy.");

        try {
            // Shutdown UPnP stack
            if (upnpService != null) {
                upnpService.shutdown();
            }
        } catch (Exception e) {
            logger.error("Chii MediaServerService destory with exception: {}.", e.getMessage());
        }
    }

    /**
     * Create UPnP Device
     *
     * @return UPnP Device
     * @throws ValidationException          thrown when the device graph being instantiated is invalid
     * @throws LocalServiceBindingException thrown when something is wrong with annotation metadata on service implementation class
     */
    public LocalDevice createUPnPDevice()
            throws ValidationException, LocalServiceBindingException {
        // Device Type
        DeviceType type =
                new UDADeviceType("MediaServer", 1);
        // XBox360 Device Details
        DeviceDetails xboxDetails = new DeviceDetails(
                "Chii2 : " + serverPrefix,
                new ManufacturerDetails("Chii2", "http://www.chii2.org/"),
                new ModelDetails("Windows Media Player Sharing", "Windows Media Player Sharing", "12"),
                "000da201238c",
                "100000000001",
                "http://www.chii2.org/some_user_interface/",
                new DLNADoc[]{
                        new DLNADoc("DMS", DLNADoc.Version.V1_5),
                },
                new DLNACaps(new String[]{
                        "av-upload", "image-upload", "audio-upload"
                })
        );

        DeviceDetails psDetails = new DeviceDetails(
                "Chii2 : " + serverPrefix,
                new ManufacturerDetails("Chii2", "http://www.chii2.org/"),
                new ModelDetails("Chii2 Home Server", "Chii2 Home Server", "1"),
                "000da201238c",
                "100000000001",
                "http://www.chii2.org/some_user_interface/",
                new DLNADoc[]{
                        new DLNADoc("DMS", DLNADoc.Version.V1_5),
                },
                new DLNACaps(new String[]{
                        "av-upload", "image-upload", "audio-upload"
                })
        );

        // Device Details Provider
        Map<HeaderDeviceDetailsProvider.Key, DeviceDetails> headerDetails = new HashMap<HeaderDeviceDetailsProvider.Key, DeviceDetails>();
        headerDetails.put(new HeaderDeviceDetailsProvider.Key("User-Agent", "Xbox.*"), xboxDetails);
        headerDetails.put(new HeaderDeviceDetailsProvider.Key("X-AV-Client-Info", ".*PLAYSTATION 3.*"), psDetails);
        HeaderDeviceDetailsProvider provider = new HeaderDeviceDetailsProvider(psDetails, headerDetails);

        // Content Directory Service
        @SuppressWarnings("unchecked")
        LocalService<ContentDirectory> contentDirectory =
                new AnnotationLocalServiceBinder().read(ContentDirectory.class);
        contentDirectory.setManager(
                new DefaultServiceManager<ContentDirectory>(contentDirectory, null) {
                    @Override
                    protected ContentDirectory createServiceInstance() throws Exception {
                        return new ContentDirectory(mediaLibrary, httpService, transcoder, onlineVideos);
                    }
                }
        );

        // Connection Manager Service
        @SuppressWarnings("unchecked")
        LocalService<ConnectionManagerService> connectionManager =
                new AnnotationLocalServiceBinder().read(ConnectionManagerService.class);
        connectionManager.setManager(
                new DefaultServiceManager<ConnectionManagerService>(
                        connectionManager,
                        ConnectionManagerService.class
                )
        );

        // Media Receiver Registrar Service
        @SuppressWarnings("unchecked")
        LocalService<MediaReceiverRegistrar> mediaReceiverRegistrar =
                new AnnotationLocalServiceBinder().read(MediaReceiverRegistrar.class);
        mediaReceiverRegistrar.setManager(
                new DefaultServiceManager<MediaReceiverRegistrar>(
                        mediaReceiverRegistrar,
                        MediaReceiverRegistrar.class
                )
        );

        // Creeate/Start UPnP/DLNA Device
        return new LocalDevice(
                new DeviceIdentity(udn),
                type,
                provider,
                createDefaultDeviceIcon(),
                new LocalService[]{connectionManager, contentDirectory, mediaReceiverRegistrar}
        );
    }

    protected Icon createDefaultDeviceIcon() {
        return new Icon(
                "image/png",
                48, 48, 24,
                URI.create("icon.png"),
                "89504E470D0A1A0A0000000D49484452000000300000003008060000005702F9870000001974455874" +
                        "536F6674776172650041646F626520496D616765526561647971C9653C00000C4649444154" +
                        "78DAEC5A596C5CD519FECF3977993B7367F312DB896367278490102814824A852210AD5AA9" +
                        "0F152D6A4B1F2AFA56A40AA15642AA2A51150955E2A1E2A1D0A66A20126291A8AA16C492D0" +
                        "24A459084BE22C4E829338C6DBD8B3CFDCB97BFF73E6CE78EC193B4EECB642EA75AEE6CEBD" +
                        "33E77CDFFF7FFF72CE84F8BE0F5FE683C297FC906A17EF5F780686AE1C86684207C76150AE" +
                        "9420DA1E0163BA02AA4A415234C866F310EB084139E780E4BB108E4721335D84509488A1AC" +
                        "A21999488F3E90CFE474C33463BEE7C700FCA8E37851C6588C50BC36DD8416D50EEA09FDD7" +
                        "F8FC86813FFDC8A7B3092CE5E0329465BA4649E82F27BB6EBEB75C32A0582C40B150845C36" +
                        "07857C1E0CC300CFF3C4E77516791D805FFBCBE781A580A744BA4F0DA97F2640D6F190D2A3" +
                        "3A44E33120E8186E65C7712097CBC189A3C7C1B66C9B52F903D722F85DD2724C4216986F39" +
                        "09708BEA31ED5155939EC791753F18DEE3D208ACCD0F940FC4E371F49204AEEB9F720A64D0" +
                        "F69DF9039379405ACEE72F130134B3B0BC449FD242F26FAE95C96ACF09A510096BEF777468" +
                        "9ED740B06E79EAA3F57D981E6E8789C90870B88DDE58DDDE0321AA40230DE906B073E01145" +
                        "969F6712FBF162D2B02F64818940924062F4A00B0E46C0ECEF1109EF9A128C0FF441FA723B" +
                        "147D1BBC396EE8613D008A0C8D734AD7AB779CB72F1A0FBF84D6BC6FD135847B0BAD2FC94A" +
                        "36EF9947CB954ADD8A1C23953CF0A6A3903BB509CAE9084AC801465AC888A2EC28A95AF1BA" +
                        "09E0776445BE5391D55711FC1AB8810218D64247BC8A3749810592C1F840F0995194CCC9F5" +
                        "C01C1598EC0066E865CE424236F0DD44BBBE1BDF456F043CB7785B32F9772D2F6350736F00" +
                        "D8B60F5F9C4940EA5C37489C0DCAE87A33AB74CD69714026B32799C29EAD4B86C08C7BC96C" +
                        "A2ADB851747BA564FD2116EFF8D3AAFE95229B8C0D1118FC1706EC17B2C83A40FD65AE03DC" +
                        "EAD457935DDAEF25457A4C90F1AA00F92B4F22F557BFEE259444F57E63D09B65FB29E2B9BF" +
                        "C5F160EA2A85818300A343145CC7C30ABFB48226CD075E924877695A79397585EEB20C0481" +
                        "A76510B02A008E05383982E7A74BEA24284AFBAE6FB9B0A2AFFA1C0F83FACE6346BEB2D7C8" +
                        "2B3080164F8F10706CFCACE2A38CFC392E5C06025C26AA466FF72BD1BDFB5F92366727ABC0" +
                        "6655483273DD98A739B9D43085951B3CD4394C64268C87B3A3CA81538725981AA548DC8770" +
                        "042755701E02B01C8DF02C02BCEC63BAFB8EAAB317478748477E1A400D2F7E302E9F4206CD" +
                        "5EF44E0C9F82EF9DFF48FD3C3D26836581C2981752541241C2D8E0F19344092109B4401CAF" +
                        "75BC17AAE221161AE80AD6B3BF62ECD833D8BC8509F8285C29C27E4E15FAAC0F9E5448B3EB" +
                        "CF0868D991B304262F33B9304D9F43207126434493650E3086468F20600E54118E23AC4523" +
                        "2492065426523FC94CA776F31B1E624B2692A06EDA0C739B893A8136BDFF1955557E59B6A6" +
                        "C1A379BF5234896539E0FABC9C1394110186D984B703F3355BFC3ED7B76DD26D4866DB2C54" +
                        "D75930E3F1E48FB0F0EDE62D87A228B06EED5AAC6164768668241051933F8DAA1D58E2F1CF" +
                        "3721FE8DB2BF7D4B9E4C8EE7B1E72FE359826CBA0CA5A209A6E90AB90962B44A8EBFF2F77C" +
                        "8E5686BD5E028AA2EEC071BB90C0C4DA75EB211CD6C0B34C5188E79390ED8217D84B262BBA" +
                        "92D0B3AA4D80E279DBB65C019E93989A2C406AA288D725C8E07B41AC6082653AE2B38208AD" +
                        "7A8C932381CB5AB51E0BB423F152B9BC614D7FFF4458D330ABB92D978F75029EE38D6093D6" +
                        "55CBA30E7EC1711AE541201A0B413CA1C1DA8D1DC2E2FC709076B96C433E67403A55446285" +
                        "2AA9C06B855C053DE604DEA2D557529562D56B34781F783048719C587B5BF2E1EEEE1E1DD7" +
                        "13157C368DE7457C5C21AD081815F3821E0DDF319F45F87DECE5F1C420C33691CBA85272C0" +
                        "C17B9ECB0D20811E8981DAAB4367073E2B3B90CF56D05B551255C01C2C6D90DB8C771AAF1B" +
                        "8CF6384AE8714685ED5DDB369F330DE3C9C620AC139024767A71A9924076CA840FDF1D03A3" +
                        "648B5CEE7112D54EB59EDBF91CD5F860A0EBFA3525E4076B8C790FC25B2839A984022FCD25" +
                        "90C9143EE9D1D405C17350AEE3C3F1031328910A76A72C584121D079338D5F5DA12D75E95A" +
                        "ADF6271499B5DE56D123A1936881F282795EA670693007A93103149555330E81FFCAC1BD73" +
                        "F7AE553BEFDA49E2EBFBC79B09A426D32396659F2164FE8576C57061F064B61EC0CB71F0F9" +
                        "F84A8D49D2026B6F1F22BA04ABD7E93FD4DA134FB3F67833810B6746FD912BA90F78AFDE5A" +
                        "FB14C6864B90CB98220E5A015908A000C958D333DE228C8E8EC2C4D858CB4016D18B3196EC" +
                        "0C01E7D8D1DDFDFDCDB76D6B6B22B0E1A65E0CB6D01BAEDB5AB03C5087CEE59A944E83ECC2" +
                        "2B662B801EA6ADB367CEC0F163C7600C81D286CF70597CF2F1C770F8D0213874F0209CFAEC" +
                        "B3D69A44449D3D61916AB18076A627B2F73411685B910459558F61FEFF68EE100C83349B36" +
                        "616ADC109E68049FCFE5E04304F0C1BE7D70EEDCB95916E4CF4F9F3E2D805DB97C198E1D39" +
                        "0253A99420CACF4C2603C3C3C320CBB278CFAFCBA592F85E63F0863406DDBD61E1094EBAB3" +
                        "2BF9D55641CC4F0F8BCA0B3533733971BD73D05F5C2E6235F69A0C343030202C5B2814E0F4" +
                        "A953303E3E2EE4C289F00DADC989090C7E5980B4F17D3A9D160045BB80F7F8675DF452CD83" +
                        "129E8DE9141581D6D7209654840A0287DCDC94468FEFBF142C4AC89E6D3B7BB7C712DAEDB9" +
                        "4C79BB2CB388E43018B9548406C308807C62BE6558D3B78B00B90549504979607674764216" +
                        "2D5D03984C26C5353FA3F1386CBDF556383F3828486DDEB205D7229A905DE38AAEA74F1786" +
                        "746B9DA80FFD4D04AE9CCF8B4931555A3BBEC67E36F0F108BCF9CA89976559FA415B471C12" +
                        "FA8A59F2119F45807D7D7D3070F22458083E8EE07A56AE14C46A3DFCD6AD5B41555528A287" +
                        "56F5F6422712AA3DE740D7AD5F0FBDAB570BD2DC4B6E23780C472D1CC8C7F11B8DD7D54420" +
                        "9D9912FC1455827D6F7F0AFBFF318020D9596E1185452019254D95924FB661D32688C56250" +
                        "42CBF7F4F4E084E1FA266ECD0BB720899976C46D1A430A52E8DC67BC4DE943EB47130AF65C" +
                        "B3DA68AD89C0E591C97ABE3F3B78155DC6908C7285EF6726DBE20B54480FBAD1EA3490D4DC" +
                        "2D430EDA719C456D3D36ED6660DFB466431B30228347B880BCE0B3BED14420149A692354B5" +
                        "B608F12FF1C584C67539CF924E580E01BACB526EF93F2216CCAE4D41EF2C81133D0DE3B938" +
                        "AA208A6718545CDC958AC685E676DA6D06E87ADEC5362D94C3E08B2F4460E1BD70527DA9C5" +
                        "9F58CD0706AA6D4A90EAA62E613CD05152211B948809ED1B2661BA9205BFCCAAA9D567AE26" +
                        "B7BFDB1BFFCAAF9A17F5446955BE26B5507808836647B37BF906D04C0AACA6577F66C78203" +
                        "12BBCD1EDF5FC26B0427E3A244728129FC744052F9690BC0FC9AF2FBB2537D2EF3551F2F5C" +
                        "2A1F336399F6AB98E15E3442C68995B1EDCDDD687767A4D5FEBFEF39E613E88907D14371AC" +
                        "84324F85C493EC823DE257E0F347BB56B6EB3C4B0171C4262D07C85FB93529AB02A7AC7A12" +
                        "7156EFD509C38C37FCBA67B0823BA2565C745DE785B261BF8260AEF22601AB52EB769A9179" +
                        "1AB88AB11F07DACF4757550DD2D369B89A7B1FD88A81DFC9AAA36DBBE5EB904854820C4266" +
                        "FD8C52950E69D0772029972EB8E181E9F39F18AFCF3BC47D0BE72EF2668E11D2B2619F5913" +
                        "B778CCEF515CA1AB54835C210D87DE3B00878F1E841DDF2EFDE2A6D5D127BA56F4414CEF00" +
                        "DBE4E0D9525A526E7EC377FDD710FC5FD056FB049A450C391303BEDD3C2E8E6483011F96DE" +
                        "8323E70FC0783105773CA43EB2F5CEF6674C5CE4F7F6AD9EB70B5D24729E1AAF7AA6B5074D" +
                        "B5D7F7E959B1C94BC8A2F7A4EA0452537B5B0C8F6B5FFCE36BDA5BC30077DFAF7D73C5CAD8" +
                        "1EBBE2115E7C14450DF689082CFAC70E12C8C9F38EA3BEFFE83AEE1BA8EF69495297B6B5F8" +
                        "767F7EDEF9B8517442430FC4238F42051C042C71CD1F3F72142BF146E85FBB4654D3850A56" +
                        "40D2F11CEF2DCFB177FBAEFB378FFF94C17F17588217EB04B291853F68128C67E23D425D58" +
                        "4B2CD82529F47ECBB2760E7C76B27F1C17239BB7DC0CED1D1DF5466D66E397EF2B7929DB76" +
                        "5E772AEE6E99B81FF924F8816119D6A33331708D3A156429AE9321CBF48780BA2F4AC48FBA" +
                        "12BD67622CF5E0D464FADEF59BD6DDBE61E34645C5AAEE792EDFFF1FB04C67AFE33A7BD0FA" +
                        "A3D4436BCB64A93BEA4BFF99B5C170052C37EF308FBE53B11CB8786E704B6A32F5D0D6DBB6" +
                        "EF0CA9E1374DC37A0D399BB5ED47F80F6C0090FFFF6F95FFF1F16F010600920311DE85F67A" +
                        "F50000000049454E44AE426082"
        );
    }

    /**
     * Inject Media Library Service
     *
     * @param mediaLibrary Media Library Service
     */
    @SuppressWarnings("unused")
    public void setMediaLibrary(MediaLibraryService mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
    }

    /**
     * Inject Media Server HTTP Server Service
     *
     * @param httpService HTTP Server Service
     */
    @SuppressWarnings("unused")
    public void setHttpService(HttpServerService httpService) {
        this.httpService = httpService;
    }

    /**
     * Inject Transcoder Service
     *
     * @param transcoder Transcoder Service
     */
    @SuppressWarnings("unused")
    public void setTranscoder(TranscoderService transcoder) {
        this.transcoder = transcoder;
    }

    /**
     * Inject Online Video Providers
     *
     * @param onlineVideos Online Video Providers
     */
    @SuppressWarnings("unused")
    public void setOnlineVideos(List<OnlineVideoProviderService> onlineVideos) {
        this.onlineVideos = onlineVideos;
    }
}
