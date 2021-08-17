jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentPositionService } from '../service/document-position.service';
import { IDocumentPosition, DocumentPosition } from '../document-position.model';
import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { CatalogUnitService } from 'app/entities/catalog-unit/service/catalog-unit.service';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note/service/delivery-note.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { OrderConfirmationService } from 'app/entities/order-confirmation/service/order-confirmation.service';

import { DocumentPositionUpdateComponent } from './document-position-update.component';

describe('Component Tests', () => {
  describe('DocumentPosition Management Update Component', () => {
    let comp: DocumentPositionUpdateComponent;
    let fixture: ComponentFixture<DocumentPositionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentPositionService: DocumentPositionService;
    let catalogUnitService: CatalogUnitService;
    let deliveryNoteService: DeliveryNoteService;
    let invoiceService: InvoiceService;
    let offerService: OfferService;
    let orderConfirmationService: OrderConfirmationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentPositionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentPositionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentPositionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentPositionService = TestBed.inject(DocumentPositionService);
      catalogUnitService = TestBed.inject(CatalogUnitService);
      deliveryNoteService = TestBed.inject(DeliveryNoteService);
      invoiceService = TestBed.inject(InvoiceService);
      offerService = TestBed.inject(OfferService);
      orderConfirmationService = TestBed.inject(OrderConfirmationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CatalogUnit query and add missing value', () => {
        const documentPosition: IDocumentPosition = { id: 456 };
        const unit: ICatalogUnit = { id: 41242 };
        documentPosition.unit = unit;

        const catalogUnitCollection: ICatalogUnit[] = [{ id: 22125 }];
        jest.spyOn(catalogUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogUnitCollection })));
        const additionalCatalogUnits = [unit];
        const expectedCollection: ICatalogUnit[] = [...additionalCatalogUnits, ...catalogUnitCollection];
        jest.spyOn(catalogUnitService, 'addCatalogUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(catalogUnitService.query).toHaveBeenCalled();
        expect(catalogUnitService.addCatalogUnitToCollectionIfMissing).toHaveBeenCalledWith(
          catalogUnitCollection,
          ...additionalCatalogUnits
        );
        expect(comp.catalogUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DeliveryNote query and add missing value', () => {
        const documentPosition: IDocumentPosition = { id: 456 };
        const deliveryNote: IDeliveryNote = { id: 63493 };
        documentPosition.deliveryNote = deliveryNote;

        const deliveryNoteCollection: IDeliveryNote[] = [{ id: 19590 }];
        jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
        const additionalDeliveryNotes = [deliveryNote];
        const expectedCollection: IDeliveryNote[] = [...additionalDeliveryNotes, ...deliveryNoteCollection];
        jest.spyOn(deliveryNoteService, 'addDeliveryNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(deliveryNoteService.query).toHaveBeenCalled();
        expect(deliveryNoteService.addDeliveryNoteToCollectionIfMissing).toHaveBeenCalledWith(
          deliveryNoteCollection,
          ...additionalDeliveryNotes
        );
        expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Invoice query and add missing value', () => {
        const documentPosition: IDocumentPosition = { id: 456 };
        const invoice: IInvoice = { id: 92001 };
        documentPosition.invoice = invoice;

        const invoiceCollection: IInvoice[] = [{ id: 15404 }];
        jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
        const additionalInvoices = [invoice];
        const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
        jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(invoiceService.query).toHaveBeenCalled();
        expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoices);
        expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Offer query and add missing value', () => {
        const documentPosition: IDocumentPosition = { id: 456 };
        const offer: IOffer = { id: 73553 };
        documentPosition.offer = offer;

        const offerCollection: IOffer[] = [{ id: 7626 }];
        jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
        const additionalOffers = [offer];
        const expectedCollection: IOffer[] = [...additionalOffers, ...offerCollection];
        jest.spyOn(offerService, 'addOfferToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(offerService.query).toHaveBeenCalled();
        expect(offerService.addOfferToCollectionIfMissing).toHaveBeenCalledWith(offerCollection, ...additionalOffers);
        expect(comp.offersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrderConfirmation query and add missing value', () => {
        const documentPosition: IDocumentPosition = { id: 456 };
        const orderConfirmation: IOrderConfirmation = { id: 14762 };
        documentPosition.orderConfirmation = orderConfirmation;

        const orderConfirmationCollection: IOrderConfirmation[] = [{ id: 72047 }];
        jest.spyOn(orderConfirmationService, 'query').mockReturnValue(of(new HttpResponse({ body: orderConfirmationCollection })));
        const additionalOrderConfirmations = [orderConfirmation];
        const expectedCollection: IOrderConfirmation[] = [...additionalOrderConfirmations, ...orderConfirmationCollection];
        jest.spyOn(orderConfirmationService, 'addOrderConfirmationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentPosition });
        comp.ngOnInit();

        expect(orderConfirmationService.query).toHaveBeenCalled();
        expect(orderConfirmationService.addOrderConfirmationToCollectionIfMissing).toHaveBeenCalledWith(
          orderConfirmationCollection,
          ...additionalOrderConfirmations
        );
        expect(comp.orderConfirmationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documentPosition: IDocumentPosition = { id: 456 };
        const unit: ICatalogUnit = { id: 22073 };
        documentPosition.unit = unit;
        const deliveryNote: IDeliveryNote = { id: 76692 };
        documentPosition.deliveryNote = deliveryNote;
        const invoice: IInvoice = { id: 21803 };
        documentPosition.invoice = invoice;
        const offer: IOffer = { id: 35592 };
        documentPosition.offer = offer;
        const orderConfirmation: IOrderConfirmation = { id: 18287 };
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
        const saveSubject = new Subject<HttpResponse<DocumentPosition>>();
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
        const saveSubject = new Subject<HttpResponse<DocumentPosition>>();
        const documentPosition = new DocumentPosition();
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
        const saveSubject = new Subject<HttpResponse<DocumentPosition>>();
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
      describe('trackCatalogUnitById', () => {
        it('Should return tracked CatalogUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogUnitById(0, entity);
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
