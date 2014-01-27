/**************************************************************************
/* This class is an OutputConsumer which saves the output to an ArrayList.
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

package org.im4java.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
   This class is an OutputConsumer which saves the output to an ArrayList.

   @version $Revision: 1.4 $
   @author  $Author: bablokb $
 
   @since 0.95
*/

public class ArrayListOutputConsumer implements OutputConsumer {

  //////////////////////////////////////////////////////////////////////////////

  /**
     The output list.
  */

  private ArrayList<String> iOutputLines = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////

  /**
     The charset-name for the internal InputStreamReader.
  */

  private String iCharset = null;

  //////////////////////////////////////////////////////////////////////////////

  /**
     Default Constructor.
  */

  public  ArrayListOutputConsumer() {
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Constructor taking a charset-name as argument.

     @param pCharset charset-name for internal InputStreamReader
  */

  public  ArrayListOutputConsumer(String pCharset) {
    iCharset = pCharset;
  }

  //////////////////////////////////////////////////////////////////////////////
  
  /**
     Return array with output-lines.
  */

  public ArrayList<String> getOutput() {
    return iOutputLines;
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
     Clear the output.
  */

  public void clear() {
    iOutputLines.clear();
  }

  //////////////////////////////////////////////////////////////////////////////

  /**
   * Read command output and save in an internal field.
   @see org.im4java.process.OutputConsumer#consumeOutput(java.io.InputStream)
  */


  public void consumeOutput(InputStream pInputStream) throws IOException {
    InputStreamReader isr = null;
    if (iCharset == null) {
      isr = new InputStreamReader(pInputStream);
    } else {
      isr = new InputStreamReader(pInputStream,iCharset);
    }
    BufferedReader reader = new BufferedReader(isr);
    String line;
    while ((line=reader.readLine()) != null) {
      iOutputLines.add(line);
    }
    reader.close();
  }
}
