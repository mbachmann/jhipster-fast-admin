jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentFreeTextFaService } from '../service/document-free-text-fa.service';
import { IDocumentFreeTextFa, DocumentFreeTextFa } from '../document-free-text-fa.model';
import { IDocumentLetterFa } from 'app/entities/document-letter-fa/document-letter-fa.model';
import { DocumentLetterFaService } from 'app/entities/document-letter-fa/service/document-letter-fa.service';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { DeliveryNoteFaService } from 'app/entities/delivery-note-fa/service/delivery-note-fa.service';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { InvoiceFaService } from 'app/entities/invoice-fa/service/invoice-fa.service';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { OfferFaService } from 'app/entities/offer-fa/service/offer-fa.service';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { OrderConfirmationFaService } from 'app/entities/order-confirmation-fa/service/order-confirmation-fa.service';

import { DocumentFreeTextFaUpdateComponent } from './document-free-text-fa-update.component';

describe('Component Tests', () => {
  describe('DocumentFreeTextFa Management Update Component', () => {
    let comp: DocumentFreeTextFaUpdateComponent;
    let fixture: ComponentFixture<DocumentFreeTextFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentFreeTextService: DocumentFreeTextFaService;
    let documentLetterService: DocumentLetterFaService;
    let deliveryNoteService: DeliveryNoteFaService;
    let invoiceService: InvoiceFaService;
    let offerService: OfferFaService;
    let orderConfirmationService: OrderConfirmationFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentFreeTextFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentFreeTextFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentFreeTextFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentFreeTextService = TestBed.inject(DocumentFreeTextFaService);
      documentLetterService = TestBed.inject(DocumentLetterFaService);
      deliveryNoteService = TestBed.inject(DeliveryNoteFaService);
      invoiceService = TestBed.inject(InvoiceFaService);
      offerService = TestBed.inject(OfferFaService);
      orderConfirmationService = TestBed.inject(OrderConfirmationFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call DocumentLetterFa query and add missing value', () => {
        const documentFreeText: IDocumentFreeTextFa = { id: 456 };
        const documentLetter: IDocumentLetterFa = { id: 14493 };
        documentFreeText.documentLetter = documentLetter;

        const documentLetterCollection: IDocumentLetterFa[] = [{ id: 51673 }];
        jest.spyOn(documentLetterService, 'query').mockReturnValue(of(new HttpResponse({ body: documentLetterCollection })));
        const additionalDocumentLetterFas = [documentLetter];
        const expectedCollection: IDocumentLetterFa[] = [...additionalDocumentLetterFas, ...documentLetterCollection];
        jest.spyOn(documentLetterService, 'addDocumentLetterFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(documentLetterService.query).toHaveBeenCalled();
        expect(documentLetterService.addDocumentLetterFaToCollectionIfMissing).toHaveBeenCalledWith(
          documentLetterCollection,
          ...additionalDocumentLetterFas
        );
        expect(comp.documentLettersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DeliveryNoteFa query and add missing value', () => {
        const documentFreeText: IDocumentFreeTextFa = { id: 456 };
        const deliveryNote: IDeliveryNoteFa = { id: 31481 };
        documentFreeText.deliveryNote = deliveryNote;

        const deliveryNoteCollection: IDeliveryNoteFa[] = [{ id: 4595 }];
        jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
        const additionalDeliveryNoteFas = [deliveryNote];
        const expectedCollection: IDeliveryNoteFa[] = [...additionalDeliveryNoteFas, ...deliveryNoteCollection];
        jest.spyOn(deliveryNoteService, 'addDeliveryNoteFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(deliveryNoteService.query).toHaveBeenCalled();
        expect(deliveryNoteService.addDeliveryNoteFaToCollectionIfMissing).toHaveBeenCalledWith(
          deliveryNoteCollection,
          ...additionalDeliveryNoteFas
        );
        expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call InvoiceFa query and add missing value', () => {
        const documentFreeText: IDocumentFreeTextFa = { id: 456 };
        const invoice: IInvoiceFa = { id: 18713 };
        documentFreeText.invoice = invoice;

        const invoiceCollection: IInvoiceFa[] = [{ id: 452 }];
        jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
        const additionalInvoiceFas = [invoice];
        const expectedCollection: IInvoiceFa[] = [...additionalInvoiceFas, ...invoiceCollection];
        jest.spyOn(invoiceService, 'addInvoiceFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(invoiceService.query).toHaveBeenCalled();
        expect(invoiceService.addInvoiceFaToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoiceFas);
        expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OfferFa query and add missing value', () => {
        const documentFreeText: IDocumentFreeTextFa = { id: 456 };
        const offer: IOfferFa = { id: 21650 };
        documentFreeText.offer = offer;

        const offerCollection: IOfferFa[] = [{ id: 64435 }];
        jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
        const additionalOfferFas = [offer];
        const expectedCollection: IOfferFa[] = [...additionalOfferFas, ...offerCollection];
        jest.spyOn(offerService, 'addOfferFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(offerService.query).toHaveBeenCalled();
        expect(offerService.addOfferFaToCollectionIfMissing).toHaveBeenCalledWith(offerCollection, ...additionalOfferFas);
        expect(comp.offersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrderConfirmationFa query and add missing value', () => {
        const documentFreeText: IDocumentFreeTextFa = { id: 456 };
        const orderConfirmation: IOrderConfirmationFa = { id: 11208 };
        documentFreeText.orderConfirmation = orderConfirmation;

        const orderConfirmationCollection: IOrderConfirmationFa[] = [{ id: 34237 }];
        jest.spyOn(orderConfirmationService, 'query').mockReturnValue(of(new HttpResponse({ body: orderConfirmationCollection })));
        const additionalOrderConfirmationFas = [orderConfirmation];
        const expectedCollection: IOrderConfirmationFa[] = [...additionalOrderConfirmationFas, ...orderConfirmationCollection];
        jest.spyOn(orderConfirmationService, 'addOrderConfirmationFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentFreeText });
        comp.ngOnInit();

        expect(orderConfirmationService.query).toHaveBeenCalled();
        expect(orderConfirmationService.addOrderConfirmationFaToCollectionIfMissing).toHaveBeenCalledWith(
          orderConfirmationCollection,
          ...additionalOrderConfirmationFas
        );
        expect(comp.orderConfirmationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documentFreeText: IDocumentFreeTextFa = { id: 456 };
        const documentLetter: IDocumentLetterFa = { id: 18440 };
        documentFreeText.documentLetter = documentLetter;
        const deliveryNote: IDeliveryNoteFa = { id: 21580 };
        documentFreeText.deliveryNote = deliveryNote;
        const invoice: IInvoiceFa = { id: 647 };
        documentFreeText.invoice = invoice;
        const offer: IOfferFa = { id: 27822 };
        documentFreeText.offer = offer;
        const orderConfirmation: IOrderConfirmationFa = { id: 69118 };
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
        const saveSubject = new Subject<HttpResponse<DocumentFreeTextFa>>();
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
        const saveSubject = new Subject<HttpResponse<DocumentFreeTextFa>>();
        const documentFreeText = new DocumentFreeTextFa();
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
        const saveSubject = new Subject<HttpResponse<DocumentFreeTextFa>>();
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
      describe('trackDocumentLetterFaById', () => {
        it('Should return tracked DocumentLetterFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocumentLetterFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDeliveryNoteFaById', () => {
        it('Should return tracked DeliveryNoteFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeliveryNoteFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackInvoiceFaById', () => {
        it('Should return tracked InvoiceFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInvoiceFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOfferFaById', () => {
        it('Should return tracked OfferFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOfferFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOrderConfirmationFaById', () => {
        it('Should return tracked OrderConfirmationFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderConfirmationFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
