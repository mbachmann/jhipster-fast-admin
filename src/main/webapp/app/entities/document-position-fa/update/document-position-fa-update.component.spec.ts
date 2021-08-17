jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentPositionFaService } from '../service/document-position-fa.service';
import { IDocumentPositionFa, DocumentPositionFa } from '../document-position-fa.model';
import { ICatalogUnitFa } from 'app/entities/catalog-unit-fa/catalog-unit-fa.model';
import { CatalogUnitFaService } from 'app/entities/catalog-unit-fa/service/catalog-unit-fa.service';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { DeliveryNoteFaService } from 'app/entities/delivery-note-fa/service/delivery-note-fa.service';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { InvoiceFaService } from 'app/entities/invoice-fa/service/invoice-fa.service';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { OfferFaService } from 'app/entities/offer-fa/service/offer-fa.service';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { OrderConfirmationFaService } from 'app/entities/order-confirmation-fa/service/order-confirmation-fa.service';

import { DocumentPositionFaUpdateComponent } from './document-position-fa-update.component';

describe('Component Tests', () => {
  describe('DocumentPositionFa Management Update Component', () => {
    let comp: DocumentPositionFaUpdateComponent;
    let fixture: ComponentFixture<DocumentPositionFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentPositionService: DocumentPositionFaService;
    let catalogUnitService: CatalogUnitFaService;
    let deliveryNoteService: DeliveryNoteFaService;
    let invoiceService: InvoiceFaService;
    let offerService: OfferFaService;
    let orderConfirmationService: OrderConfirmationFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentPositionFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentPositionFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentPositionFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentPositionService = TestBed.inject(DocumentPositionFaService);
      catalogUnitService = TestBed.inject(CatalogUnitFaService);
      deliveryNoteService = TestBed.inject(DeliveryNoteFaService);
      invoiceService = TestBed.inject(InvoiceFaService);
      offerService = TestBed.inject(OfferFaService);
      orderConfirmationService = TestBed.inject(OrderConfirmationFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CatalogUnitFa query and add missing value', () => {
        const documentPosition: IDocumentPositionFa = { id: 456 };
        const unit: ICatalogUnitFa = { id: 41242 };
        documentPosition.unit = unit;

        const catalogUnitCollection: ICatalogUnitFa[] = [{ id: 22125 }];
        jest.spyOn(catalogUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogUnitCollection })));
        const additionalCatalogUnitFas = [unit];
        const expectedCollection: ICatalogUnitFa[] = [...additionalCatalogUnitFas, ...catalogUnitCollection];
        jest.spyOn(catalogUnitService, 'addCatalogUnitFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(catalogUnitService.query).toHaveBeenCalled();
        expect(catalogUnitService.addCatalogUnitFaToCollectionIfMissing).toHaveBeenCalledWith(
          catalogUnitCollection,
          ...additionalCatalogUnitFas
        );
        expect(comp.catalogUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DeliveryNoteFa query and add missing value', () => {
        const documentPosition: IDocumentPositionFa = { id: 456 };
        const deliveryNote: IDeliveryNoteFa = { id: 63493 };
        documentPosition.deliveryNote = deliveryNote;

        const deliveryNoteCollection: IDeliveryNoteFa[] = [{ id: 19590 }];
        jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
        const additionalDeliveryNoteFas = [deliveryNote];
        const expectedCollection: IDeliveryNoteFa[] = [...additionalDeliveryNoteFas, ...deliveryNoteCollection];
        jest.spyOn(deliveryNoteService, 'addDeliveryNoteFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(deliveryNoteService.query).toHaveBeenCalled();
        expect(deliveryNoteService.addDeliveryNoteFaToCollectionIfMissing).toHaveBeenCalledWith(
          deliveryNoteCollection,
          ...additionalDeliveryNoteFas
        );
        expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call InvoiceFa query and add missing value', () => {
        const documentPosition: IDocumentPositionFa = { id: 456 };
        const invoice: IInvoiceFa = { id: 92001 };
        documentPosition.invoice = invoice;

        const invoiceCollection: IInvoiceFa[] = [{ id: 15404 }];
        jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
        const additionalInvoiceFas = [invoice];
        const expectedCollection: IInvoiceFa[] = [...additionalInvoiceFas, ...invoiceCollection];
        jest.spyOn(invoiceService, 'addInvoiceFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(invoiceService.query).toHaveBeenCalled();
        expect(invoiceService.addInvoiceFaToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoiceFas);
        expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OfferFa query and add missing value', () => {
        const documentPosition: IDocumentPositionFa = { id: 456 };
        const offer: IOfferFa = { id: 73553 };
        documentPosition.offer = offer;

        const offerCollection: IOfferFa[] = [{ id: 7626 }];
        jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
        const additionalOfferFas = [offer];
        const expectedCollection: IOfferFa[] = [...additionalOfferFas, ...offerCollection];
        jest.spyOn(offerService, 'addOfferFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(offerService.query).toHaveBeenCalled();
        expect(offerService.addOfferFaToCollectionIfMissing).toHaveBeenCalledWith(offerCollection, ...additionalOfferFas);
        expect(comp.offersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrderConfirmationFa query and add missing value', () => {
        const documentPosition: IDocumentPositionFa = { id: 456 };
        const orderConfirmation: IOrderConfirmationFa = { id: 14762 };
        documentPosition.orderConfirmation = orderConfirmation;

        const orderConfirmationCollection: IOrderConfirmationFa[] = [{ id: 72047 }];
        jest.spyOn(orderConfirmationService, 'query').mockReturnValue(of(new HttpResponse({ body: orderConfirmationCollection })));
        const additionalOrderConfirmationFas = [orderConfirmation];
        const expectedCollection: IOrderConfirmationFa[] = [...additionalOrderConfirmationFas, ...orderConfirmationCollection];
        jest.spyOn(orderConfirmationService, 'addOrderConfirmationFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(orderConfirmationService.query).toHaveBeenCalled();
        expect(orderConfirmationService.addOrderConfirmationFaToCollectionIfMissing).toHaveBeenCalledWith(
          orderConfirmationCollection,
          ...additionalOrderConfirmationFas
        );
        expect(comp.orderConfirmationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documentPosition: IDocumentPositionFa = { id: 456 };
        const unit: ICatalogUnitFa = { id: 22073 };
        documentPosition.unit = unit;
        const deliveryNote: IDeliveryNoteFa = { id: 76692 };
        documentPosition.deliveryNote = deliveryNote;
        const invoice: IInvoiceFa = { id: 21803 };
        documentPosition.invoice = invoice;
        const offer: IOfferFa = { id: 35592 };
        documentPosition.offer = offer;
        const orderConfirmation: IOrderConfirmationFa = { id: 18287 };
        documentPosition.orderConfirmation = orderConfirmation;

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documentPosition));
        expect(comp.catalogUnitsSharedCollection).toContain(unit);
        expect(comp.deliveryNotesSharedCollection).toContain(deliveryNote);
        expect(comp.invoicesSharedCollection).toContain(invoice);
        expect(comp.offersSharedCollection).toContain(offer);
        expect(comp.orderConfirmationsSharedCollection).toContain(orderConfirmation);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentPositionFa>>();
        const documentPosition = { id: 123 };
        jest.spyOn(documentPositionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentPosition }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentPositionService.update).toHaveBeenCalledWith(documentPosition);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentPositionFa>>();
        const documentPosition = new DocumentPositionFa();
        jest.spyOn(documentPositionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentPosition }));
        saveSubject.complete();

        // THEN
        expect(documentPositionService.create).toHaveBeenCalledWith(documentPosition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentPositionFa>>();
        const documentPosition = { id: 123 };
        jest.spyOn(documentPositionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentPositionService.update).toHaveBeenCalledWith(documentPosition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCatalogUnitFaById', () => {
        it('Should return tracked CatalogUnitFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogUnitFaById(0, entity);
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
