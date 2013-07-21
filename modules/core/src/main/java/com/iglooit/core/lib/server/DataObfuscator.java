package com.iglooit.core.lib.server;

import com.clarity.commons.iface.domain.SystemDateProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class implements methods useful when obfuscating data strings.
 *
 * The functionality of this class exists in two locations: <br>
 * <ul>
 *     <li>The clarity-bss-core module</li>
 *     <li>The CLARITY_UTILITIES project in real time</li>
 * </ul>
 *
 * It is <b>VERY IMPORTANT</b> that any changes made to the class in one
 * of those locations is made in both locations.
 */
public class DataObfuscator
{
    private static final Random GPRBG = new Random(SystemDateProvider.now().getTime());
    private static final SortedSet<Integer> PRIME_NUMBERS;

    static
    {
        PRIME_NUMBERS = new TreeSet<Integer>();
        PRIME_NUMBERS.add(2);
        PRIME_NUMBERS.add(3);
        PRIME_NUMBERS.add(5);
        PRIME_NUMBERS.add(7);
        PRIME_NUMBERS.add(11);
        PRIME_NUMBERS.add(13);
    }

    /**
     * Get an indication as to whether or not the specified number is
     * in the set of prime numbers less than or equal to 13.
     */
    private static boolean isPrime(int number)
    {
        return PRIME_NUMBERS.contains(number);
    }

    /**
     * This method will generate a random sequence of bytes in one of two
     * sequences, either a set of bytes where the four low order bits are
     * prime followed by a single byte where the four low order bits are
     * non prime or vice versa.
     *
     * @return A set of bytes.
     */
    private static List<Byte> getLeadInSet()
    {
        ArrayList<Byte> leadInSet = new ArrayList<Byte>();
        byte []         ba  = new byte[1];

        GPRBG.nextBytes(ba);
        if (((int)(ba[0] & 0x01)) != 0)
        {
            // The lead in bytes will be a set of 'non primary' bytes followed
            // by a byte that is 'primary'.
            while (isPrime((int)(ba[0] & 0x0f)))
                GPRBG.nextBytes(ba);
            while (!isPrime((int)(ba[0] & 0x0f)))
            {
                leadInSet.add(ba[0]);
                GPRBG.nextBytes(ba);
            }
            leadInSet.add(ba[0]);
        }
        else
        {
            // The lead in bytes will be a set of 'primary' bytes followed
            // by a byte that is 'non primary'.
            while (!isPrime((int)(ba[0] & 0x0f)))
                GPRBG.nextBytes(ba);
            while (isPrime((int)(ba[0] & 0x0f)))
            {
                leadInSet.add(ba[0]);
                GPRBG.nextBytes(ba);
            }
            leadInSet.add(ba[0]);
        }

        return leadInSet;
    }

    /**
     * This method will return a copy of the source array minus
     * the lead in set of bytes.
     *
     * @see #getLeadInSet()
     */
    private static byte [] removeLeadInSet(byte [] source)
    {
        int i = 0;

        if (isPrime((int)(source[0] & 0x0f)))
        {
            while (isPrime((int)(source[i] & 0x0f)))
                i += 1;
        }
        else
        {
            while (!isPrime((int)(source[i] & 0x0f)))
                i += 1;
        }
        i += 1;

        return subArray(source, i);
    }

    /**
     * This method will convert an {@link java.util.List} of {@link Byte} to an
     * array of byte.
     */
    private static byte [] convert(List<Byte> in)
    {
        int     i = 0;
        byte [] out = new byte [in.size()];

        for (Byte b : in)
            out[i++] = b;

        return out;
    }

    /**
     * This method will return a sub array of the specified source array starting
     * with the byte at startIndex.
     */
    private static byte [] subArray(byte [] source, int startIndex)
    {
        byte [] subArray = new byte [source.length - startIndex];
        int     j        = 0;

        for (int i = startIndex; i < source.length; i++)
            subArray[j++] = source[i];

        return subArray;
    }

    /**
     * Given a string value which was previously obfuscated by the method {@link #getObfuscatedText(String)}
     * this method will return the clear text.
     *
     * @param obfuscatedText The obfuscated text.
     * @return               The clear text.
     */
    public static String getClearText(String obfuscatedText)
    {
        byte []         extendedXoredText = new Base64Encoder().decode(obfuscatedText);
        ArrayList<Byte> xoredText         = new ArrayList<Byte>();
        byte []         textAsBytes;
        byte []         randomBytes;

        while (extendedXoredText.length > 0)
        {
            extendedXoredText = removeLeadInSet(extendedXoredText);
            xoredText.add(extendedXoredText[0]);
            extendedXoredText = subArray(extendedXoredText, 1);
        }

        textAsBytes = new byte [xoredText.size()];
        randomBytes = new byte [xoredText.size()];
        getByteGenerator(randomBytes.length).nextBytes(randomBytes);
        for (int i = 0; i < textAsBytes.length; i++)
            textAsBytes[i] = (byte)(xoredText.get(i) ^ randomBytes[i]);

        return new String(textAsBytes, StandardCharsets.UTF_8);
    }

    /**
     * This method will return a string value created by obfuscating the specified clear text.
     *
     * @param clearText  The clear text.
     * @return           The obfuscated text.
     */
    public static String getObfuscatedText(String clearText)
    {
        byte []         textAsBytes       = clearText.getBytes(StandardCharsets.UTF_8);
        byte []         xoredText         = new byte [textAsBytes.length];
        byte []         randomBytes       = new byte [textAsBytes.length];
        ArrayList<Byte> extendedXoredText = new ArrayList<Byte>();

        getByteGenerator(randomBytes.length).nextBytes(randomBytes);
        for (int i = 0; i < xoredText.length; i++)
            xoredText[i] = (byte)(textAsBytes[i] ^ randomBytes[i]);

        for (int i = 0; i < xoredText.length; i++)
        {
            extendedXoredText.addAll(getLeadInSet());
            extendedXoredText.add(xoredText[i]);
        }

        return new Base64Encoder().encode(convert(extendedXoredText));
    }

    /**
     * This method will return an initialised byte generator. The sequence of bytes generated by the
     * method {@link java.util.Random#nextBytes(byte[])} will be consistent across invocations.
     */
    private static Random getByteGenerator(int s)
    {
        String s1 = "lkjh(*&kjhoiu)(*UKJ09870OIIUL;Kkj_)(*_)(ip:lK-098_)(iplkl:khuydY5*&_)iPOK=-)(+-POL'[;l_)" +
                    "8oIUHJ0(&0978ujPoi-08_)oi[Po(*0";
        String s2 = "KJH(*&^*(&KJ:LKJMHBuyhtgjhg*&^Tkj(87yKJh(*876LkjH*765DgfKj:LKP)8)9iu;lKPO98(*&;loi)987)" +
                    "(*&08uLKJLKh(*7p;oikp9oiu)(8)9uP";
        String s3 = "Clarity International Limited Level 3, 15 Blue Street North Sydney, NSW 2060 Australia";
        long      hs = (long)(s1.hashCode()) << 32;
        long      ls = s2.hashCode();
        long      ms = ((long)s3.hashCode()) << s;

        return new Random((hs + ms) + ls);
    }

    /**
     * THERE ARE MANY IMPLEMENTATIONS OF THE BASE64 DECODER ALGORITHM. THIS HAPPENS TO BE
     * THE ONE THAT IS USED IN REAL TIME. 
     *
     * This class implements a base 64 encoder. A base 64 encoder will accept bytes
     * and encode them into a set of printable characters. The size of the output
     * will be on the order of 30% greater than the input. For each three input bytes
     * four characters will be produced.
     */
    private static final class Base64Encoder
    {
        private static final char   PADDING_CHAR  = '=';
        private static final char   LF = 10;
        private static final char   CR = 13;
        private boolean             sunCompatible = false;

        private char [] encodingChars =
        {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '+', '/'
        };

        /**
         * Constructs a new encoder. The resulting encoder is compatible with
         * the Sun encoder.
         */
        private Base64Encoder()
        {
            this(true);
        }

        /**
         * Constructs a new encoder. Sun corporation distributes a base 64 encoder (which application
         * software should not use) in the sun.misc package.
         * If the parameter supplied to this constructor is true the newly instantiated encoder will be
         * compatible with Sun's encoder. The output of a Sun compatible encoder tends to be longer as Sun breaks
         * the output up into records separated by line separator characters.
         *
         * @param sunCompatible true if the encoder is to be compatible with the Sun encoder, otherwise false.
         */
        private Base64Encoder(boolean sunCompatible)
        {
            this.sunCompatible = sunCompatible;
        }

        /**
         * Encode the specified array of bytes into a string.
         *
         * @param in The array of bytes to be encoded.
         * @return An encoded string which may be supplied as a parameter to the decode method
         * to be converted back to an identical array ot bytes.
         */
        private String encode(byte [] in)
        {
            CharArrayWriter out = new CharArrayWriter((in.length * 4) / 3);

            try
            {
                encode(new ByteArrayInputStream(in), out);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            return new String(out.toCharArray());
        }

        /**
         * Reads and encodes bytes from the specified input stream and writes the resulting
         * characters to the output writer. The method continues until the input stream
         * returns end of stream.
         *
         * @param in The stream from which bytes are read and encoded.
         * @param out The writer to which encoding characters are written. The characters written
         * to this writer may be supplied to the decode method to be decoded back to the original
         * bytes.
         */
        private void encode(InputStream in, Writer out)
            throws IOException
        {
            String lineSeparator   = System.getProperty("line.separator");
            long    outCount        = 0;
            int     data            = in.read();
            boolean finished        = data == -1;
            byte    prevByte        = 0;


            // Encode the bytes.
            for (int byteNumber = 0; !finished; byteNumber++)
            {
                byte    thisByte;


                // Get the next byte to be encoded.
                if (byteNumber > 0 && data != -1)
                {
                    data = in.read();
                    finished = data == -1;
                }
                thisByte = finished ? 0 : (byte)data;


                // Sun puts out a new line sequence after 76 characters so we do as well.
                // Note, we don't count the line separator characters.
                if (sunCompatible && outCount > 0 && outCount % 76 == 0)
                    out.write(lineSeparator);


                // Create the encoding character from any bits remaining from the previous byte
                // and bits in this byte.
                switch(byteNumber % 3)
                {
                    case 0:
                        if (!finished)
                        {
                            out.write(encodingChars[(thisByte >> 2) & 0x3f]);
                            outCount++;
                            prevByte = (byte)((thisByte << 4) & 0x3f);
                        }
                        break;

                    case 1:
                        out.write(encodingChars[prevByte | ((thisByte >> 4) & 0x0f)]);
                        outCount++;
                        prevByte = (byte)((thisByte << 2) & 0x3f);
                        break;

                    case 2:
                        out.write(encodingChars[prevByte | ((thisByte >> 6) & 0x03)]);
                        outCount++;
                        if (!finished)
                        {
                            out.write(encodingChars[thisByte & 0x3f]);
                            outCount++;
                        }
                        break;

                    default:
                        throw new RuntimeException("An impossible event has occurred.");
                }
            }


            // Pad the output to multiples of four characters.
            while (outCount % 4 != 0)
            {
                out.write(PADDING_CHAR);
                outCount++;
            }


            out.flush();
        }


        /**
         * Decode the characters in the specified string to an array of bytes.
         *
         * @param in The string of characters to be decoded.
         * @return An array of bytes which are identical to those bytes supplied to the encode method.
         *
         * @exception IllegalArgumentException Is thrown if any invalid encoding characters
         * are encountered while performing the decode operation or the number of bytes supplied
         * is not an even multiple of four.
         */
        private byte [] decode(String in)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream((in.length() * 3) / 4);

            try
            {
                decode(new CharArrayReader(in.toCharArray()), out);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            return out.toByteArray();
        }


        /**
         * Reads and dencodes characters from the specified input reader and writes the resulting
         * bytes to the output stream. The method continues until the input reader
         * returns end of input.
         *
         * @param in The reader from which encoding characters are read and decoded.
         * @param out The output stream to which decoded bytes are written.
         *
         * @exception IllegalArgumentException Is thrown if any invalid encoding characters
         * are encountered while performing the decode operation or the number of bytes supplied
         * is not an even multiple of four.
         */
        private void decode(Reader in, OutputStream out)
            throws IOException
        {
            byte    completeByte = 0;
            int     data;
            int     charNumber;

            for (charNumber = 0; true; charNumber++)
            {
                data = in.read();                
                if (data == -1)
                    break;

                char    encodingChar = (char)data;
                byte    newBits;


                // Take care of any padding characters.
                if (encodingChar == PADDING_CHAR)
                    continue;

                // Take care of any line sparator characters.
                if (sunCompatible)
                {
                    // Note, we don't count the line separator characters.
                    while (encodingChar == CR || encodingChar == LF)
                    {
                        data = in.read();
                        encodingChar = (char)(data);
                    }

                    if (data == -1)
                        break;
                }


                // Determine the 6 bits which are encoded by this character.
                if (encodingChar == '/')
                    newBits = 63;
                else if (encodingChar == '+')
                    newBits = 62;
                else if (encodingChar >= '0' && encodingChar <= '9')
                    newBits = (byte)(26 + 26 + (encodingChar - '0'));
                else if (encodingChar >= 'a' && encodingChar <= 'z')
                    newBits = (byte)(26 + (encodingChar - 'a'));
                else if (encodingChar >= 'A' && encodingChar <= 'Z')
                    newBits = (byte)(encodingChar - 'A');
                else
                    throw new IllegalArgumentException(
                        "Invalid encoding character encountered: " + encodingChar + " (" + data + ")"
                    );


                // Form the byte and write it to the output stream if it is complete.
                switch(charNumber % 4)
                {
                    case 0:
                        completeByte = (byte)(newBits << 2);
                        break;

                    case 1:
                        completeByte |= newBits >> 4;
                        out.write(completeByte);
                        completeByte = (byte)(newBits << 4);
                        break;

                    case 2:
                        completeByte |= newBits >> 2;
                        out.write(completeByte);
                        completeByte = (byte)(newBits << 6);
                        break;

                    case 3:
                        completeByte |= newBits;
                        out.write(completeByte);
                        break;

                    default:
                        throw new RuntimeException("An impossible event has occurred!");
                }
            }

            out.flush();

            if (charNumber % 4 != 0)
                throw new IllegalArgumentException(
                    "The number of input bytes must be an even multiple of four."
                );
        }
    }
}
