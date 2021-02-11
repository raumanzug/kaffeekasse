/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Rectangle;
import dom.jfischer.kaffeekasse.backend.DAO;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * implements {@link dom.jfischer.kaffeekasse.middleTier.TallyPDF}.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class TallyPDFImpl implements TallyPDF {

    /**
     * width of left margin.
     */
    private static final int DOCUMENT_LEFT_MARGIN = 37;

    /**
     * width of right margin.
     */
    private static final int DOCUMENT_RIGHT_MARGIN = 37;

    /**
     * height of top margin.
     */
    private static final int DOCUMENT_TOP_MARGIN = 37;

    /**
     * height of bottom margin.
     */
    private static final int DOCUMENT_BOTTOM_MARGIN = 37;

    /**
     * width of participant's table column.
     */
    private static final float TABLE_PARTICIPANT_MARGIN = 37F;

    /**
     * width of deposit's table column.
     */
    private static final float TABLE_DEPOSIT_MARGIN = 12F;

    /**
     * width of table column which contains number of cups.
     */
    private static final float TABLE_NR_CUPS_MARGIN = 12F;

    /**
     * width of table column where participants can make strokes.
     */
    private static final float TABLE_TALLY_MARGIN = 144F;

    /**
     * backend.
     */
    private final DAO dao;

    /**
     * for notifying frontend about errors.
     */
    private final SlaveImpl slave;

    /**
     * constructor.
     *
     * @param dao a {@link dom.jfischer.kaffeekasse.backend.DAO} object.
     * @param slave a {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl}
     * object.
     */
    public TallyPDFImpl(final DAO dao, final SlaveImpl slave) {
        this.dao = dao;
        this.slave = slave;
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link TallyPDF#storeTally}.
     */
    @Override
    public void storeTally(final String filename) {
        this.dao.open();

        AccountPeriod currentAccountPeriod
                = this.dao.getCurrentAccountPeriod();
        int price = currentAccountPeriod.getPrice();

        Rectangle pageSize = PageSize.A4;
        Document document
                = new Document(
                        pageSize,
                        DOCUMENT_LEFT_MARGIN,
                        DOCUMENT_RIGHT_MARGIN,
                        DOCUMENT_TOP_MARGIN,
                        DOCUMENT_BOTTOM_MARGIN);
        try {
            FileOutputStream ostream = new FileOutputStream(filename);
            PdfWriter.getInstance(document, ostream);

            document.addCreator("kaffeekasse v0.1");

            document.open();

            Paragraph coffeePriceParagraph
                    = new Paragraph("Kaffeepreis: " + price + " cent");
            document.add(coffeePriceParagraph);

            float[] margins = {
                TABLE_PARTICIPANT_MARGIN,
                TABLE_DEPOSIT_MARGIN,
                TABLE_NR_CUPS_MARGIN,
                TABLE_TALLY_MARGIN};
            PdfPTable table
                    = new PdfPTable(margins);
            table.setHorizontalAlignment(0);
            table.addCell("Teilnehmer");
            table.addCell("Guthaben");
            table.addCell("#Tassen");
            table.addCell("Strichliste");

            List<Participant> participants = this.dao.listActiveParticipants();
            for (Participant participant : participants) {
                table.addCell(participant.getName());
                table.addCell("" + participant.getDeposit());
                table.addCell("" + participant.getNrCups());
                table.addCell("");
            }

            document.add(table);

        } catch (DocumentException | FileNotFoundException exc) {
            this.slave.processTallyPDFError(exc.getMessage());
        }
        document.close();

        this.dao.close();

    }

}
