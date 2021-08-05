jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactAddressMySuffixService } from '../service/contact-address-my-suffix.service';
import { IContactAddressMySuffix, ContactAddressMySuffix } from '../contact-address-my-suffix.model';

import { ContactAddressMySuffixUpdateComponent } from './contact-address-my-suffix-update.component';

describe('Component Tests', () => {
  describe('ContactAddressMySuffix Management Update Component', () => {
    let comp: ContactAddressMySuffixUpdateComponent;
    let fixture: ComponentFixture<ContactAddressMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactAddressService: ContactAddressMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactAddressMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactAddressMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactAddressMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactAddressService = TestBed.inject(ContactAddressMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const contactAddress: IContactAddressMySuffix = { id: 456 };

        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactAddress));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAddressMySuffix>>();
        const contactAddress = { id: 123 };
        jest.spyOn(contactAddressService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactAddress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactAddressService.update).toHaveBeenCalledWith(contactAddress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAddressMySuffix>>();
        const contactAddress = new ContactAddressMySuffix();
        jest.spyOn(contactAddressService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactAddress }));
        saveSubject.complete();

        // THEN
        expect(contactAddressService.create).toHaveBeenCalledWith(contactAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAddressMySuffix>>();
        const contactAddress = { id: 123 };
        jest.spyOn(contactAddressService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactAddressService.update).toHaveBeenCalledWith(contactAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
