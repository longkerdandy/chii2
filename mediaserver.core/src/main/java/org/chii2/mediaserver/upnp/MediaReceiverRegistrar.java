package org.chii2.mediaserver.upnp;

import org.teleal.cling.binding.annotations.UpnpAction;
import org.teleal.cling.binding.annotations.UpnpInputArgument;
import org.teleal.cling.binding.annotations.UpnpOutputArgument;
import org.teleal.cling.binding.annotations.UpnpService;
import org.teleal.cling.binding.annotations.UpnpServiceId;
import org.teleal.cling.binding.annotations.UpnpServiceType;
import org.teleal.cling.binding.annotations.UpnpStateVariable;
import org.teleal.cling.binding.annotations.UpnpStateVariables;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;

import java.beans.PropertyChangeSupport;

/**
 * Basic implementation of service required by MSFT devices such as XBox 360.
 *
 * @author Mario Franco
 */
@UpnpService(
        serviceId = @UpnpServiceId(
                namespace = "microsoft.com",
                value = "X_MS_MediaReceiverRegistrar"
        ),
        serviceType = @UpnpServiceType(
                namespace = "microsoft.com",
                value = "X_MS_MediaReceiverRegistrar",
                version = 1
        )
)
@UpnpStateVariables(
        {
                @UpnpStateVariable(name = "A_ARG_TYPE_DeviceID",
                                   sendEvents = false,
                                   datatype = "string"),
                @UpnpStateVariable(name = "A_ARG_TYPE_Result",
                                   sendEvents = false,
                                   datatype = "boolean"),
                @UpnpStateVariable(name = "A_ARG_TYPE_RegistrationReqMsg",
                                   sendEvents = false,
                                   datatype = "bin.base64"),
                @UpnpStateVariable(name = "A_ARG_TYPE_RegistrationRespMsg",
                                   sendEvents = false,
                                   datatype = "bin.base64")
        }
)
public class MediaReceiverRegistrar{

    final protected PropertyChangeSupport propertyChangeSupport;

    @UpnpStateVariable(eventMinimumDelta = 1)
    private UnsignedIntegerFourBytes authorizationGrantedUpdateID = new UnsignedIntegerFourBytes(0);

    @UpnpStateVariable(eventMinimumDelta = 1)
    private UnsignedIntegerFourBytes authorizationDeniedUpdateID = new UnsignedIntegerFourBytes(0);

    @UpnpStateVariable
    private UnsignedIntegerFourBytes validationSucceededUpdateID = new UnsignedIntegerFourBytes(0);

    @UpnpStateVariable
    private UnsignedIntegerFourBytes validationRevokedUpdateID = new UnsignedIntegerFourBytes(0);

    protected MediaReceiverRegistrar() {
        this(null);
    }

    protected MediaReceiverRegistrar(PropertyChangeSupport propertyChangeSupport) {
        this.propertyChangeSupport = propertyChangeSupport != null ? propertyChangeSupport : new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }


    @UpnpAction(out = @UpnpOutputArgument(name = "AuthorizationGrantedUpdateID"))
    public UnsignedIntegerFourBytes getAuthorizationGrantedUpdateID() {
        return authorizationGrantedUpdateID;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "AuthorizationDeniedUpdateID"))
    public UnsignedIntegerFourBytes getAuthorizationDeniedUpdateID() {
        return authorizationDeniedUpdateID;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ValidationSucceededUpdateID"))
    public UnsignedIntegerFourBytes getValidationSucceededUpdateID() {
        return validationSucceededUpdateID;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ValidationRevokedUpdateID"))
    public UnsignedIntegerFourBytes getValidationRevokedUpdateID() {
        return validationRevokedUpdateID;
    }

    @UpnpAction(out = {
            @UpnpOutputArgument(name = "Result",
                                stateVariable = "A_ARG_TYPE_Result")
    })
    public boolean isAuthorized(@UpnpInputArgument(name = "DeviceID",
                                                   stateVariable = "A_ARG_TYPE_DeviceID")
                                String deviceID) {
        return true;
    }

    @UpnpAction(out = {
            @UpnpOutputArgument(name = "Result",
                                stateVariable = "A_ARG_TYPE_Result")
    })
    public boolean isValidated(@UpnpInputArgument(name = "DeviceID",
                                                  stateVariable = "A_ARG_TYPE_DeviceID")
                               String deviceID) {
        return false;
    }

    @UpnpAction(out = {
            @UpnpOutputArgument(name = "RegistrationRespMsg",
                                stateVariable = "A_ARG_TYPE_RegistrationRespMsg")
    })
    public byte[] registerDevice(@UpnpInputArgument(name = "RegistrationReqMsg",
                                                    stateVariable = "A_ARG_TYPE_RegistrationReqMsg")
                                 byte[] registrationReqMsg) {
        return new byte[]{};
    }
}

