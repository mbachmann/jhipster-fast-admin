jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DescriptiveDocumentTextFaService } from '../service/descriptive-document-text-fa.service';
import { IDescriptiveDocumentTextFa, DescriptiveDocumentTextFa } from '../descriptive-document-text-fa.model';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { DeliveryNoteFaService } from 'app/entities/delivery-note-fa/service/delivery-note-fa.service';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { InvoiceFaService } from 'app/entities/invoice-fa/service/invoice-fa.service';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { OfferFaService } from 'app/entities/offer-fa/service/offer-fa.service';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { OrderConfirmationFaService } from 'app/entities/order-confirmation-fa/service/order-confirmation-fa.service';

import { DescriptiveDocumentTextFaUpdateComponent } from './descriptive-document-text-fa-update.component';

describe('Component Tests', () => {
  describe('DescriptiveDocumentTextFa Management Update Component', () => {
    let comp: DescriptiveDocumentTextFaUpdateComponent;
    let fixture: ComponentFixture<DescriptiveDocumentTextFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let descriptiveDocumentTextService: DescriptiveDocumentTextFaService;
    let deliveryNoteService: DeliveryNoteFaService;
    let invoiceService: InvoiceFaService;
    let offerService: OfferFaService;
    let orderConfirmationService: OrderConfirmationFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DescriptiveDocumentTextFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DescriptiveDocumentTextFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DescriptiveDocumentTextFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      descriptiveDocumentTextService = TestBed.inject(DescriptiveDocumentTextFaService);
      deliveryNoteService = TestBed.inject(DeliveryNoteFaService);
      invoiceService = TestBed.inject(InvoiceFaService);
      offerService = TestBed.inject(OfferFaService);
      orderConfirmationService = TestBed.inject(OrderConfirmationFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call DeliveryNoteFa query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 456 };
        const deliveryNote: IDeliveryNoteFa = { id: 72900 };
        descriptiveDocumentText.deliveryNote = deliveryNote;

        const deliveryNoteCollection: IDeliveryNoteFa[] = [{ id: 91587 }];
        jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
        const additionalDeliveryNoteFas = [deliveryNote];
        const expectedCollection: IDeliveryNoteFa[] = [...additionalDeliveryNoteFas, ...deliveryNoteCollection];
        jest.spyOn(deliveryNoteService, 'addDeliveryNoteFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(deliveryNoteService.query).toHaveBeenCalled();
        expect(deliveryNoteService.addDeliveryNoteFaToCollectionIfMissing).toHaveBeenCalledWith(
          deliveryNoteCollection,
          ...additionalDeliveryNoteFas
        );
        expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call InvoiceFa query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 456 };
        const invoice: IInvoiceFa = { id: 95582 };
        descriptiveDocumentText.invoice = invoice;

        const invoiceCollection: IInvoiceFa[] = [{ id: 75886 }];
        jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
        const additionalInvoiceFas = [invoice];
        const expectedCollection: IInvoiceFa[] = [...additionalInvoiceFas, ...invoiceCollection];
        jest.spyOn(invoiceService, 'addInvoiceFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(invoiceService.query).toHaveBeenCalled();
        expect(invoiceService.addInvoiceFaToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoiceFas);
        expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OfferFa query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 456 };
        const offer: IOfferFa = { id: 26528 };
        descriptiveDocumentText.offer = offer;

        const offerCollection: IOfferFa[] = [{ id: 14364 }];
        jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
        const additionalOfferFas = [offer];
        const expectedCollection: IOfferFa[] = [...additionalOfferFas, ...offerCollection];
        jest.spyOn(offerService, 'addOfferFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(offerService.query).toHaveBeenCalled();
        expect(offerService.addOfferFaToCollectionIfMissing).toHaveBeenCalledWith(offerCollection, ...additionalOfferFas);
        expect(comp.offersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrderConfirmationFa query and add missing value', () => {
        const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 456 };
        const orderConfirmation: IOrderConfirmationFa = { id: 31446 };
        descriptiveDocumentText.orderConfirmation = orderConfirmation;

        const orderConfirmationCollection: IOrderConfirmationFa[] = [{ id: 61558 }];
        jest.spyOn(orderConfirmationService, 'query').mockReturnValue(of(new HttpResponse({ body: orderConfirmationCollection })));
        const additionalOrderConfirmationFas = [orderConfirmation];
        const expectedCollection: IOrderConfirmationFa[] = [...additionalOrderConfirmationFas, ...orderConfirmationCollection];
        jest.spyOn(orderConfirmationService, 'addOrderConfirmationFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ descriptiveDocumentText });
        comp.ngOnInit();

        expect(orderConfirmationService.query).toHaveBeenCalled();
        expect(orderConfirmationService.addOrderConfirmationFaToCollectionIfMissing).toHaveBeenCalledWith(
          orderConfirmationCollection,
          ...additionalOrderConfirmationFas
        );
        expect(comp.orderConfirmationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 456 };
        const deliveryNote: IDeliveryNoteFa = { id: 39021 };
        descriptiveDocumentText.deliveryNote = deliveryNote;
        const invoice: IInvoiceFa = { id: 73559 };
        descriptiveDocumentText.invoice = invoice;
        const offer: IOfferFa = { id: 22888 };
        descriptiveDocumentText.offer = offer;
        const orderConfirmation: IOrderConfirmationFa = { id: 55767 };
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
        const saveSubject = new Subject<HttpResponse<DescriptiveDocumentTextFa>>();
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
        const saveSubject = new Subject<HttpResponse<DescriptiveDocumentTextFa>>();
        const descriptiveDocumentText = new DescriptiveDocumentTextFa();
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
        const saveSubject = new Subject<HttpResponse<DescriptiveDocumentTextFa>>();
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
