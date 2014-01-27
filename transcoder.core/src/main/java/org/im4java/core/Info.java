/**************************************************************************
/* This class implements an image-information object.
/*
/* Copyright (c) 2009 by Bernhard Bablok (mail@bablokb.de)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published
/* by  the Free Software Foundation; either version 2 of the License or
/* (at your option) any later version.
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this program; see the file COPYING.LIB.  If not, write to
/* the Free Software Foundation Inc., 59 Temple Place - Suite 330,
/* Boston, MA  02111-1307 USA
/**************************************************************************/

package org.im4java.core;

import java.util.*;
import java.io.*;

import org.im4java.process.ArrayListOutputConsumer;

/**
   This class implements an image-information object.

   <p>The class just calls "identify -verbose" and parses the output.</p>
   
   @version $Revision: 1.8 $
   @author  $Author: bablokb $
 
   @since 0.95
*/

public class  Info {

  //////////////////////////////////////////////////////////////////////////////

  /**
     Internal hashtable with image-attributes.
  */

  private Hashtable<String,String> iAttributes = new Hashtable<String,String>();

  //////////////////////////////////////////////////////////////////////////////

  /**
     Current value of indentation level
  */

  private int iOldIndent=0;

  //////////////////////////////////////////////////////////////////////////////

  /**
     Current value of attribute-prefix
  */

  private String iPrefix="";

  //////////////////////////////////////////////////////////////////////////////

  /**
     This contstructor will automatically parse the full output
     of identify -verbose.
 
     @param pImage  Source image
     @since 0.95
  */

  public Info(String pImage) throws InfoException {
    getCompleteInfo(pImage);
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     This constructor creates an Info-object with basic or complete
     image-information (depending on the second argument).
 
     @param pImage  Source image
     @param basic   Set to true for basic information, to false for complete info
     @since 1.2.0
  */

  public Info(String pImage, boolean basic) throws InfoException {

    if (!basic) {
      getCompleteInfo(pImage);
    } else {
      getBaseInfo(pImage);
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Query complete image-information.
 
     @param pImage  Source image
     @since 1.2.0
  */

  private void getCompleteInfo(String pImage) throws InfoException {
    IMOperation op = new IMOperation();
    op.verbose();
    op.addImage(pImage);

    try {
      IdentifyCmd identify = new IdentifyCmd();
      ArrayListOutputConsumer output = new ArrayListOutputConsumer();
      identify.setOutputConsumer(output);
      identify.run(op);
      ArrayList<String> cmdOutput = output.getOutput();
      for (String line:cmdOutput) {
	parseLine(line);
      }
    } catch (Exception ex) {
      throw new InfoException(ex);
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Parse line of identify-output
  */

  private void parseLine(String pLine) {
    // structure:
    //    indent attribute: value


    int indent = pLine.indexOf(pLine.trim())/2;
    String[] parts = pLine.trim().split(": ",2);

    // check indentation level and remove prefix if necessary
    if (indent < iOldIndent) {
      // remove tokens from iPrefix
      int colonIndex=iPrefix.length()-1;
      for (int i=0;i<iOldIndent-indent;++i) {
	colonIndex = iPrefix.lastIndexOf(':',colonIndex-1);
      }
      if (colonIndex == -1) {
	iPrefix="";
      } else {
	iPrefix=iPrefix.substring(0,colonIndex+1);
      }
    }
    iOldIndent = indent;

    // add a new attribute or increase prefix
    if (parts.length == 1) {
      // no value => add attribute to attribute-prefix
      iPrefix=iPrefix+parts[0];
    } else {
      // value => add (key,value) to attributes
      iAttributes.put(iPrefix+parts[0],parts[1]);
    }
  }


  //////////////////////////////////////////////////////////////////////////////

  /**
     Query basic image-information.
 
     @param pImage  Source image
     @since 1.2.0
  */

  private void getBaseInfo(String pImage) throws InfoException {
    // create operation
    IMOperation op = new IMOperation();
    op.ping();
    op.format("%m\n%W\n%H\n%g\n%z\n%r");
    op.addImage(pImage);

    try {
      // execute ...
      IdentifyCmd identify = new IdentifyCmd();
      ArrayListOutputConsumer output = new ArrayListOutputConsumer();
      identify.setOutputConsumer(output);
      identify.run(op);

      // ... and parse result
      ArrayList<String> cmdOutput = output.getOutput();
      Iterator<String> iter = cmdOutput.iterator();
      iAttributes.put("Format",iter.next());
      iAttributes.put("Width",iter.next());
      iAttributes.put("Height",iter.next());
      iAttributes.put("Geometry",iter.next());
      iAttributes.put("Depth",iter.next());
      iAttributes.put("Class",iter.next());
    } catch (Exception ex) {
      throw new InfoException(ex);
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return the image format.
  */

  public String getImageFormat() {
    return iAttributes.get("Format");
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return the image width.
  */

  public int getImageWidth() throws InfoException {
    try {
      return Integer.parseInt(iAttributes.get("Width"));
    } catch (NumberFormatException ex) {
      throw new InfoException(ex);
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return the image height.
  */

  public int getImageHeight() throws InfoException {
    try {
      return Integer.parseInt(iAttributes.get("Height"));
    } catch (NumberFormatException ex) {
      throw new InfoException(ex);
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return the image depth.
  */

  public int getImageDepth() throws InfoException {
    try {
      return Integer.parseInt(iAttributes.get("Depth"));
    } catch (NumberFormatException ex) {
      throw new InfoException(ex);
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return the image geometry.
  */

  public String getImageGeometry() {
    return iAttributes.get("Geometry");
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return the image class.
  */

  public String getImageClass() {
    return iAttributes.get("Class");
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return the given property.
  */

  public String getProperty(String pPropertyName) {
    return iAttributes.get(pPropertyName);
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Return an enumeration of all properties.
  */

  public Enumeration<String> getPropertyNames() {
    return iAttributes.keys();
  }
}
