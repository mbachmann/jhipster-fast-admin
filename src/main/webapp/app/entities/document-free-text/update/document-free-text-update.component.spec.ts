jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentFreeTextService } from '../service/document-free-text.service';
import { IDocumentFreeText, DocumentFreeText } from '../document-free-text.model';
import { IDocumentLetter } from 'app/entities/document-letter/document-letter.model';
import { DocumentLetterService } from 'app/entities/document-letter/service/document-letter.service';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note/service/delivery-note.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { OrderConfirmationService } from 'app/entities/order-confirmation/service/order-confirmation.service';

import { DocumentFreeTextUpdateComponent } from './document-free-text-update.component';

describe('Component Tests', () => {
  describe('DocumentFreeText Management Update Component', () => {
    let comp: DocumentFreeTextUpdateComponent;
    let fixture: ComponentFixture<DocumentFreeTextUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentFreeTextService: DocumentFreeTextService;
    let documentLetterService: DocumentLetterService;
    let deliveryNoteService: DeliveryNoteService;
    let invoiceService: InvoiceService;
    let offerService: OfferService;
    let orderConfirmationService: OrderConfirmationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentFreeTextUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentFreeTextUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentFreeTextUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentFreeTextService = TestBed.inject(DocumentFreeTextService);
      documentLetterService = TestBed.inject(DocumentLetterService);
      deliveryNoteService = TestBed.inject(DeliveryNoteService);
      invoiceService = TestBed.inject(InvoiceService);
      offerService = TestBed.inject(OfferService);
      orderConfirmationService = TestBed.inject(OrderConfirmationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call DocumentLetter query and add missing value', () => {
        const documentFreeText: IDocumentFreeText = { id: 456 };
        const documentLetter: IDocumentLetter = { id: 14493 };
        documentFreeText.documentLetter = documentLetter;

        const documentLetterCollection: IDocumentLetter[] = [{ id: 51673 }];
        jest.spyOn(documentLetterService, 'query').mockReturnValue(of(new HttpResponse({ body: documentLetterCollection })));
        const additionalDocumentLetters = [documentLetter];
        const expectedCollection: IDocumentLetter[] = [...additionalDocumentLetters, ...documentLetterCollection];
        jest.spyOn(documentLetterService, 'addDocumentLetterToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(documentLetterService.query).toHaveBeenCalled();
        expect(documentLetterService.addDocumentLetterToCollectionIfMissing).toHaveBeenCalledWith(
          documentLetterCollection,
          ...additionalDocumentLetters
        );
        expect(comp.documentLettersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DeliveryNote query and add missing value', () => {
        const documentFreeText: IDocumentFreeText = { id: 456 };
        const deliveryNote: IDeliveryNote = { id: 31481 };
        documentFreeText.deliveryNote = deliveryNote;

        const deliveryNoteCollection: IDeliveryNote[] = [{ id: 4595 }];
        jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
        const additionalDeliveryNotes = [deliveryNote];
        const expectedCollection: IDeliveryNote[] = [...additionalDeliveryNotes, ...deliveryNoteCollection];
        jest.spyOn(deliveryNoteService, 'addDeliveryNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(deliveryNoteService.query).toHaveBeenCalled();
        expect(deliveryNoteService.addDeliveryNoteToCollectionIfMissing).toHaveBeenCalledWith(
          deliveryNoteCollection,
          ...additionalDeliveryNotes
        );
        expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Invoice query and add missing value', () => {
        const documentFreeText: IDocumentFreeText = { id: 456 };
        const invoice: IInvoice = { id: 18713 };
        documentFreeText.invoice = invoice;

        const invoiceCollection: IInvoice[] = [{ id: 452 }];
        jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
        const additionalInvoices = [invoice];
        const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
        jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(invoiceService.query).toHaveBeenCalled();
        expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoices);
        expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Offer query and add missing value', () => {
        const documentFreeText: IDocumentFreeText = { id: 456 };
        const offer: IOffer = { id: 21650 };
        documentFreeText.offer = offer;

        const offerCollection: IOffer[] = [{ id: 64435 }];
        jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
        const additionalOffers = [offer];
        const expectedCollection: IOffer[] = [...additionalOffers, ...offerCollection];
        jest.spyOn(offerService, 'addOfferToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(offerService.query).toHaveBeenCalled();
        expect(offerService.addOfferToCollectionIfMissing).toHaveBeenCalledWith(offerCollection, ...additionalOffers);
        expect(comp.offersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrderConfirmation query and add missing value', () => {
        const documentFreeText: IDocumentFreeText = { id: 456 };
        const orderConfirmation: IOrderConfirmation = { id: 11208 };
        documentFreeText.orderConfirmation = orderConfirmation;

        const orderConfirmationCollection: IOrderConfirmation[] = [{ id: 34237 }];
        jest.spyOn(orderConfirmationService, 'query').mockReturnValue(of(new HttpResponse({ body: orderConfirmationCollection })));
        const additionalOrderConfirmations = [orderConfirmation];
        const expectedCollection: IOrderConfirmation[] = [...additionalOrderConfirmations, ...orderConfirmationCollection];
        jest.spyOn(orderConfirmationService, 'addOrderConfirmationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(orderConfirmationService.query).toHaveBeenCalled();
        expect(orderConfirmationService.addOrderConfirmationToCollectionIfMissing).toHaveBeenCalledWith(
          orderConfirmationCollection,
          ...additionalOrderConfirmations
        );
        expect(comp.orderConfirmationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documentFreeText: IDocumentFreeText = { id: 456 };
        const documentLetter: IDocumentLetter = { id: 18440 };
        documentFreeText.documentLetter = documentLetter;
        const deliveryNote: IDeliveryNote = { id: 21580 };
        documentFreeText.deliveryNote = deliveryNote;
        const invoice: IInvoice = { id: 647 };
        documentFreeText.invoice = invoice;
        const offer: IOffer = { id: 27822 };
        documentFreeText.offer = offer;
        const orderConfirmation: IOrderConfirmation = { id: 69118 };
        documentFreeText.orderConfirmation = orderConfirmation;

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documentFreeText));
        expect(comp.documentLettersSharedCollection).toContain(documentLetter);
        expect(comp.deliveryNotesSharedCollection).toContain(deliveryNote);
        expect(comp.invoicesSharedCollection).toContain(invoice);
        expect(comp.offersSharedCollection).toContain(offer);
        expect(comp.orderConfirmationsSharedCollection).toContain(orderConfirmation);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentFreeText>>();
        const documentFreeText = { id: 123 };
        jest.spyOn(documentFreeTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentFreeText }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentFreeTextService.update).toHaveBeenCalledWith(documentFreeText);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentFreeText>>();
        const documentFreeText = new DocumentFreeText();
        jest.spyOn(documentFreeTextService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentFreeText }));
        saveSubject.complete();

        // THEN
        expect(documentFreeTextService.create).toHaveBeenCalledWith(documentFreeText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentFreeText>>();
        const documentFreeText = { id: 123 };
        jest.spyOn(documentFreeTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentFreeTextService.update).toHaveBeenCalledWith(documentFreeText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDocumentLetterById', () => {
        it('Should return tracked DocumentLetter primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocumentLetterById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDeliveryNoteById', () => {
        it('Should return tracked DeliveryNote primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeliveryNoteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackInvoiceById', () => {
        it('Should return tracked Invoice primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInvoiceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOfferById', () => {
        it('Should return tracked Offer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOfferById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOrderConfirmationById', () => {
        it('Should return tracked OrderConfirmation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderConfirmationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
