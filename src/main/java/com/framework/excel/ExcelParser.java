package com.framework.excel;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * 利用POI解析excel文件的工具类（注意，excel文件格式需要是xlsx）
 * Created by guoyongzheng on 15/6/18.
 */
public class ExcelParser {

    /**
     * handler 实例
     */
    private final Map<Integer, ExcelParseHandler> handlers;

    /**
     * 构造函数。需要指定handler集合（注意：index从0开始）。
     * @param handlers
     */
    public ExcelParser(Map<Integer, ExcelParseHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * 解析xlsx文件
     * @param file
     * @throws OpenXML4JException
     * @throws IOException
     * @throws SAXException
     */
    public void parseXlsx(InputStream file) throws OpenXML4JException, IOException, SAXException, ParserConfigurationException {

        OPCPackage pkg = OPCPackage.open(file);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();

        Iterator<InputStream> sheets = r.getSheetsData();
        int index = 0;
        while(sheets.hasNext()) {
            XMLReader parser = fetchSheetParser(sst, handlers.get(index++));
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst, ExcelParseHandler handler) throws SAXException, ParserConfigurationException {

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader parser = saxParser.getXMLReader();
        ContentHandler ch = new SheetHandler(sst, handler);
        parser.setContentHandler(ch);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;

        private final ExcelParseHandler handler;

        private String row = "";
        private String col = "";

        private int rowNum = 0;

        private SheetHandler(SharedStringsTable sst, ExcelParseHandler handler) {
            this.sst = sst;
            this.handler = handler;
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            //System.out.print("name=" + name + "; ");

            // start row
            if (name.equals("row")) {
                if (handler != null) {
                    handler.startRow(++rowNum);
                }
            }

            // c => cell
            if(name.equals("c")) {
                // the cell reference
                String ref = attributes.getValue("r");
                // 从ref中获取row 和 col
                row = ref.replaceAll("[A-Z]+", "");
                col = ref.replaceAll("\\d+", "");

                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }

            // end row
            if (name.equals("row")) {
                if (handler != null) {
                    if (!handler.isSkip()) {
                        handler.endRow(Integer.parseInt(row));
                    }
                    handler.endSkipRow();
                }
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v")) {
                //System.out.print(lastContents);
                if (handler != null && !handler.isSkip()) {
                    switch (col) {
                        case "A":
                            handler.handleA(lastContents);
                            break;
                        case "B":
                            handler.handleB(lastContents);
                            break;
                        case "C":
                            handler.handleC(lastContents);
                            break;
                        case "D":
                            handler.handleD(lastContents);
                            break;
                        case "E":
                            handler.handleE(lastContents);
                            break;
                        case "F":
                            handler.handleF(lastContents);
                            break;
                        case "G":
                            handler.handleG(lastContents);
                            break;
                        case "H":
                            handler.handleH(lastContents);
                            break;
                        case "I":
                            handler.handleI(lastContents);
                            break;
                        case "J":
                            handler.handleJ(lastContents);
                            break;
                        case "K":
                            handler.handleK(lastContents);
                            break;
                        case "L":
                            handler.handleL(lastContents);
                            break;
                        case "M":
                            handler.handleM(lastContents);
                            break;
                        case "N":
                            handler.handleN(lastContents);
                            break;
                        case "O":
                            handler.handleO(lastContents);
                            break;
                        case "P":
                            handler.handleP(lastContents);
                            break;
                        case "Q":
                            handler.handleQ(lastContents);
                            break;
                        case "R":
                            handler.handleR(lastContents);
                            break;
                        case "S":
                            handler.handleS(lastContents);
                            break;
                        case "T":
                            handler.handleT(lastContents);
                            break;
                        case "U":
                            handler.handleU(lastContents);
                            break;
                        case "V":
                            handler.handleV(lastContents);
                            break;
                        case "W":
                            handler.handleW(lastContents);
                            break;
                        case "X":
                            handler.handleX(lastContents);
                            break;
                        case "Y":
                            handler.handleY(lastContents);
                            break;
                        case "Z":
                            handler.handleZ(lastContents);
                            break;
                        default:
                            // do nothing
                            return;
                    }
                }
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }
    }
}
