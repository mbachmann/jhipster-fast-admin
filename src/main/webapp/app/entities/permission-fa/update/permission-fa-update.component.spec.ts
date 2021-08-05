jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PermissionFaService } from '../service/permission-fa.service';
import { IPermissionFa, PermissionFa } from '../permission-fa.model';
import { IRoleFa } from 'app/entities/role-fa/role-fa.model';
import { RoleFaService } from 'app/entities/role-fa/service/role-fa.service';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

import { PermissionFaUpdateComponent } from './permission-fa-update.component';

describe('Component Tests', () => {
  describe('PermissionFa Management Update Component', () => {
    let comp: PermissionFaUpdateComponent;
    let fixture: ComponentFixture<PermissionFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let permissionService: PermissionFaService;
    let roleService: RoleFaService;
    let contactService: ContactFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PermissionFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PermissionFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PermissionFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      permissionService = TestBed.inject(PermissionFaService);
      roleService = TestBed.inject(RoleFaService);
      contactService = TestBed.inject(ContactFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call RoleFa query and add missing value', () => {
        const permission: IPermissionFa = { id: 456 };
        const role: IRoleFa = { id: 23442 };
        permission.role = role;

        const roleCollection: IRoleFa[] = [{ id: 66478 }];
        jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
        const additionalRoleFas = [role];
        const expectedCollection: IRoleFa[] = [...additionalRoleFas, ...roleCollection];
        jest.spyOn(roleService, 'addRoleFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        expect(roleService.query).toHaveBeenCalled();
        expect(roleService.addRoleFaToCollectionIfMissing).toHaveBeenCalledWith(roleCollection, ...additionalRoleFas);
        expect(comp.rolesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactFa query and add missing value', () => {
        const permission: IPermissionFa = { id: 456 };
        const contact: IContactFa = { id: 14692 };
        permission.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 64014 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const permission: IPermissionFa = { id: 456 };
        const role: IRoleFa = { id: 96832 };
        permission.role = role;
        const contact: IContactFa = { id: 23817 };
        permission.contact = contact;

        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(permission));
        expect(comp.rolesSharedCollection).toContain(role);
        expect(comp.contactsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PermissionFa>>();
        const permission = { id: 123 };
        jest.spyOn(permissionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: permission }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(permissionService.update).toHaveBeenCalledWith(permission);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PermissionFa>>();
        const permission = new PermissionFa();
        jest.spyOn(permissionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: permission }));
        saveSubject.complete();

        // THEN
        expect(permissionService.create).toHaveBeenCalledWith(permission);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PermissionFa>>();
        const permission = { id: 123 };
        jest.spyOn(permissionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(permissionService.update).toHaveBeenCalledWith(permission);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRoleFaById', () => {
        it('Should return tracked RoleFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRoleFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactFaById', () => {
        it('Should return tracked ContactFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
