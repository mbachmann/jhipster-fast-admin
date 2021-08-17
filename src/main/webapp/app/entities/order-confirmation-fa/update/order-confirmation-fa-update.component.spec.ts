jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderConfirmationFaService } from '../service/order-confirmation-fa.service';
import { IOrderConfirmationFa, OrderConfirmationFa } from '../order-confirmation-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { ContactAddressFaService } from 'app/entities/contact-address-fa/service/contact-address-fa.service';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ContactPersonFaService } from 'app/entities/contact-person-fa/service/contact-person-fa.service';
import { ILayoutFa } from 'app/entities/layout-fa/layout-fa.model';
import { LayoutFaService } from 'app/entities/layout-fa/service/layout-fa.service';
import { ISignatureFa } from 'app/entities/signature-fa/signature-fa.model';
import { SignatureFaService } from 'app/entities/signature-fa/service/signature-fa.service';

import { OrderConfirmationFaUpdateComponent } from './order-confirmation-fa-update.component';

describe('Component Tests', () => {
  describe('OrderConfirmationFa Management Update Component', () => {
    let comp: OrderConfirmationFaUpdateComponent;
    let fixture: ComponentFixture<OrderConfirmationFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderConfirmationService: OrderConfirmationFaService;
    let contactService: ContactFaService;
    let contactAddressService: ContactAddressFaService;
    let contactPersonService: ContactPersonFaService;
    let layoutService: LayoutFaService;
    let signatureService: SignatureFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderConfirmationFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrderConfirmationFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderConfirmationFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orderConfirmationService = TestBed.inject(OrderConfirmationFaService);
      contactService = TestBed.inject(ContactFaService);
      contactAddressService = TestBed.inject(ContactAddressFaService);
      contactPersonService = TestBed.inject(ContactPersonFaService);
      layoutService = TestBed.inject(LayoutFaService);
      signatureService = TestBed.inject(SignatureFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactFa query and add missing value', () => {
        const orderConfirmation: IOrderConfirmationFa = { id: 456 };
        const contact: IContactFa = { id: 27878 };
        orderConfirmation.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 88493 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactAddressFa query and add missing value', () => {
        const orderConfirmation: IOrderConfirmationFa = { id: 456 };
        const contactAddress: IContactAddressFa = { id: 77301 };
        orderConfirmation.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddressFa = { id: 53585 };
        orderConfirmation.contactPrePageAddress = contactPrePageAddress;

        const contactAddressCollection: IContactAddressFa[] = [{ id: 58787 }];
        jest.spyOn(contactAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: contactAddressCollection })));
        const additionalContactAddressFas = [contactAddress, contactPrePageAddress];
        const expectedCollection: IContactAddressFa[] = [...additionalContactAddressFas, ...contactAddressCollection];
        jest.spyOn(contactAddressService, 'addContactAddressFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(contactAddressService.query).toHaveBeenCalled();
        expect(contactAddressService.addContactAddressFaToCollectionIfMissing).toHaveBeenCalledWith(
          contactAddressCollection,
          ...additionalContactAddressFas
        );
        expect(comp.contactAddressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPersonFa query and add missing value', () => {
        const orderConfirmation: IOrderConfirmationFa = { id: 456 };
        const contactPerson: IContactPersonFa = { id: 4503 };
        orderConfirmation.contactPerson = contactPerson;

        const contactPersonCollection: IContactPersonFa[] = [{ id: 83263 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPersonFas = [contactPerson];
        const expectedCollection: IContactPersonFa[] = [...additionalContactPersonFas, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonFaToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPersonFas
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LayoutFa query and add missing value', () => {
        const orderConfirmation: IOrderConfirmationFa = { id: 456 };
        const layout: ILayoutFa = { id: 32764 };
        orderConfirmation.layout = layout;

        const layoutCollection: ILayoutFa[] = [{ id: 72461 }];
        jest.spyOn(layoutService, 'query').mockReturnValue(of(new HttpResponse({ body: layoutCollection })));
        const additionalLayoutFas = [layout];
        const expectedCollection: ILayoutFa[] = [...additionalLayoutFas, ...layoutCollection];
        jest.spyOn(layoutService, 'addLayoutFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(layoutService.query).toHaveBeenCalled();
        expect(layoutService.addLayoutFaToCollectionIfMissing).toHaveBeenCalledWith(layoutCollection, ...additionalLayoutFas);
        expect(comp.layoutsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SignatureFa query and add missing value', () => {
        const orderConfirmation: IOrderConfirmationFa = { id: 456 };
        const layout: ISignatureFa = { id: 80323 };
        orderConfirmation.layout = layout;

        const signatureCollection: ISignatureFa[] = [{ id: 42880 }];
        jest.spyOn(signatureService, 'query').mockReturnValue(of(new HttpResponse({ body: signatureCollection })));
        const additionalSignatureFas = [layout];
        const expectedCollection: ISignatureFa[] = [...additionalSignatureFas, ...signatureCollection];
        jest.spyOn(signatureService, 'addSignatureFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(signatureService.query).toHaveBeenCalled();
        expect(signatureService.addSignatureFaToCollectionIfMissing).toHaveBeenCalledWith(signatureCollection, ...additionalSignatureFas);
        expect(comp.signaturesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const orderConfirmation: IOrderConfirmationFa = { id: 456 };
        const contact: IContactFa = { id: 50522 };
        orderConfirmation.contact = contact;
        const contactAddress: IContactAddressFa = { id: 25228 };
        orderConfirmation.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddressFa = { id: 93673 };
        orderConfirmation.contactPrePageAddress = contactPrePageAddress;
        const contactPerson: IContactPersonFa = { id: 78852 };
        orderConfirmation.contactPerson = contactPerson;
        const layout: ILayoutFa = { id: 88212 };
        orderConfirmation.layout = layout;
        const layout: ISignatureFa = { id: 26103 };
        orderConfirmation.layout = layout;

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orderConfirmation));
        expect(comp.contactsSharedCollection).toContain(contact);
        expect(comp.contactAddressesSharedCollection).toContain(contactAddress);
        expect(comp.contactAddressesSharedCollection).toContain(contactPrePageAddress);
        expect(comp.contactPeopleSharedCollection).toContain(contactPerson);
        expect(comp.layoutsSharedCollection).toContain(layout);
        expect(comp.signaturesSharedCollection).toContain(layout);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderConfirmationFa>>();
        const orderConfirmation = { id: 123 };
        jest.spyOn(orderConfirmationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderConfirmation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orderConfirmationService.update).toHaveBeenCalledWith(orderConfirmation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderConfirmationFa>>();
        const orderConfirmation = new OrderConfirmationFa();
        jest.spyOn(orderConfirmationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderConfirmation }));
        saveSubject.complete();

        // THEN
        expect(orderConfirmationService.create).toHaveBeenCalledWith(orderConfirmation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderConfirmationFa>>();
        const orderConfirmation = { id: 123 };
        jest.spyOn(orderConfirmationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orderConfirmationService.update).toHaveBeenCalledWith(orderConfirmation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackContactFaById', () => {
        it('Should return tracked ContactFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactAddressFaById', () => {
        it('Should return tracked ContactAddressFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactAddressFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactPersonFaById', () => {
        it('Should return tracked ContactPersonFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactPersonFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLayoutFaById', () => {
        it('Should return tracked LayoutFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLayoutFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSignatureFaById', () => {
        it('Should return tracked SignatureFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSignatureFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
