jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DescriptiveDocumentTextService } from '../service/descriptive-document-text.service';
import { IDescriptiveDocumentText, DescriptiveDocumentText } from '../descriptive-document-text.model';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note/service/delivery-note.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { OrderConfirmationService } from 'app/entities/order-confirmation/service/order-confirmation.service';

import { DescriptiveDocumentTextUpdateComponent } from './descriptive-document-text-update.component';

describe('Component Tests', () => {
  describe('DescriptiveDocumentText Management Update Component', () => {
    let comp: DescriptiveDocumentTextUpdateComponent;
    let fixture: ComponentFixture<DescriptiveDocumentTextUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let descriptiveDocumentTextService: DescriptiveDocumentTextService;
    let deliveryNoteService: DeliveryNoteService;
    let invoiceService: InvoiceService;
    let offerService: OfferService;
    let orderConfirmationService: OrderConfirmationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DescriptiveDocumentTextUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DescriptiveDocumentTextUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DescriptiveDocumentTextUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      descriptiveDocumentTextService = TestBed.inject(DescriptiveDocumentTextService);
      deliveryNoteService = TestBed.inject(DeliveryNoteService);
      invoiceService = TestBed.inject(InvoiceService);
      offerService = TestBed.inject(OfferService);
      orderConfirmationService = TestBed.inject(OrderConfirmationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call DeliveryNote query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentText = { id: 456 };
        const deliveryNote: IDeliveryNote = { id: 72900 };
        descriptiveDocumentText.deliveryNote = deliveryNote;

        const deliveryNoteCollection: IDeliveryNote[] = [{ id: 91587 }];
        jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
        const additionalDeliveryNotes = [deliveryNote];
        const expectedCollection: IDeliveryNote[] = [...additionalDeliveryNotes, ...deliveryNoteCollection];
        jest.spyOn(deliveryNoteService, 'addDeliveryNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(deliveryNoteService.query).toHaveBeenCalled();
        expect(deliveryNoteService.addDeliveryNoteToCollectionIfMissing).toHaveBeenCalledWith(
          deliveryNoteCollection,
          ...additionalDeliveryNotes
        );
        expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Invoice query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentText = { id: 456 };
        const invoice: IInvoice = { id: 95582 };
        descriptiveDocumentText.invoice = invoice;

        const invoiceCollection: IInvoice[] = [{ id: 75886 }];
        jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
        const additionalInvoices = [invoice];
        const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
        jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(invoiceService.query).toHaveBeenCalled();
        expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoices);
        expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Offer query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentText = { id: 456 };
        const offer: IOffer = { id: 26528 };
        descriptiveDocumentText.offer = offer;

        const offerCollection: IOffer[] = [{ id: 14364 }];
        jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
        const additionalOffers = [offer];
        const expectedCollection: IOffer[] = [...additionalOffers, ...offerCollection];
        jest.spyOn(offerService, 'addOfferToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(offerService.query).toHaveBeenCalled();
        expect(offerService.addOfferToCollectionIfMissing).toHaveBeenCalledWith(offerCollection, ...additionalOffers);
        expect(comp.offersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrderConfirmation query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentText = { id: 456 };
        const orderConfirmation: IOrderConfirmation = { id: 31446 };
        descriptiveDocumentText.orderConfirmation = orderConfirmation;

        const orderConfirmationCollection: IOrderConfirmation[] = [{ id: 61558 }];
        jest.spyOn(orderConfirmationService, 'query').mockReturnValue(of(new HttpResponse({ body: orderConfirmationCollection })));
        const additionalOrderConfirmations = [orderConfirmation];
        const expectedCollection: IOrderConfirmation[] = [...additionalOrderConfirmations, ...orderConfirmationCollection];
        jest.spyOn(orderConfirmationService, 'addOrderConfirmationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(orderConfirmationService.query).toHaveBeenCalled();
        expect(orderConfirmationService.addOrderConfirmationToCollectionIfMissing).toHaveBeenCalledWith(
          orderConfirmationCollection,
          ...additionalOrderConfirmations
        );
        expect(comp.orderConfirmationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const descriptiveDocumentText: IDescriptiveDocumentText = { id: 456 };
        const deliveryNote: IDeliveryNote = { id: 39021 };
        descriptiveDocumentText.deliveryNote = deliveryNote;
        const invoice: IInvoice = { id: 73559 };
        descriptiveDocumentText.invoice = invoice;
        const offer: IOffer = { id: 22888 };
        descriptiveDocumentText.offer = offer;
        const orderConfirmation: IOrderConfirmation = { id: 55767 };
        descriptiveDocumentText.orderConfirmation = orderConfirmation;

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(descriptiveDocumentText));
        expect(comp.deliveryNotesSharedCollection).toContain(deliveryNote);
        expect(comp.invoicesSharedCollection).toContain(invoice);
        expect(comp.offersSharedCollection).toContain(offer);
        expect(comp.orderConfirmationsSharedCollection).toContain(orderConfirmation);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DescriptiveDocumentText>>();
        const descriptiveDocumentText = { id: 123 };
        jest.spyOn(descriptiveDocumentTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: descriptiveDocumentText }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(descriptiveDocumentTextService.update).toHaveBeenCalledWith(descriptiveDocumentText);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DescriptiveDocumentText>>();
        const descriptiveDocumentText = new DescriptiveDocumentText();
        jest.spyOn(descriptiveDocumentTextService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: descriptiveDocumentText }));
        saveSubject.complete();

        // THEN
        expect(descriptiveDocumentTextService.create).toHaveBeenCalledWith(descriptiveDocumentText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DescriptiveDocumentText>>();
        const descriptiveDocumentText = { id: 123 };
        jest.spyOn(descriptiveDocumentTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(descriptiveDocumentTextService.update).toHaveBeenCalledWith(descriptiveDocumentText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
