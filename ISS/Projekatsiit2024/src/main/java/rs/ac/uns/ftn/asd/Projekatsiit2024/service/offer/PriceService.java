package rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer;

import java.nio.file.AccessDeniedException;
import java.util.function.Function;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferRepository;

@Service
public class PriceService {
	@Autowired
	public OfferRepository offerRepo;
	
	public byte[] GeneratePdf() throws AccessDeniedException {
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!up.getUser().getRole().getName().equals("PROVIDER_ROLE"))
			throw new AccessDeniedException(null);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document doc = new Document(pdf);
		
		Table table = new Table(5);
		table.addHeaderCell(new Paragraph("Offer ID"));
		table.addHeaderCell(new Paragraph("Name"));
		table.addHeaderCell(new Paragraph("Price"));
		table.addHeaderCell(new Paragraph("Discount"));
		table.addHeaderCell(new Paragraph("True cost"));
		
		offerRepo.findLatestOffersByOfferID().stream()
			.forEach(o -> {
				table.addCell(new Paragraph(o.getOfferID().toString()));
				table.addCell(new Paragraph(o.getName()));
				table.addCell(new Paragraph(o.getPrice().toString() + " €"));
				table.addCell(new Paragraph(o.getDiscount().toString() + " €"));
				Double cost = o.getPrice() - o.getDiscount();
				table.addCell(new Paragraph(cost.toString() + " €"));
			});
		
		doc.add(table);
		doc.close();
		
		return baos.toByteArray();
	}
}
