jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PermissionMySuffixService } from '../service/permission-my-suffix.service';
import { IPermissionMySuffix, PermissionMySuffix } from '../permission-my-suffix.model';
import { IRoleMySuffix } from 'app/entities/role-my-suffix/role-my-suffix.model';
import { RoleMySuffixService } from 'app/entities/role-my-suffix/service/role-my-suffix.service';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { ContactMySuffixService } from 'app/entities/contact-my-suffix/service/contact-my-suffix.service';

import { PermissionMySuffixUpdateComponent } from './permission-my-suffix-update.component';

describe('Component Tests', () => {
  describe('PermissionMySuffix Management Update Component', () => {
    let comp: PermissionMySuffixUpdateComponent;
    let fixture: ComponentFixture<PermissionMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let permissionService: PermissionMySuffixService;
    let roleService: RoleMySuffixService;
    let contactService: ContactMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PermissionMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PermissionMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PermissionMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      permissionService = TestBed.inject(PermissionMySuffixService);
      roleService = TestBed.inject(RoleMySuffixService);
      contactService = TestBed.inject(ContactMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call RoleMySuffix query and add missing value', () => {
        const permission: IPermissionMySuffix = { id: 456 };
        const role: IRoleMySuffix = { id: 23442 };
        permission.role = role;

        const roleCollection: IRoleMySuffix[] = [{ id: 66478 }];
        jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
        const additionalRoleMySuffixes = [role];
        const expectedCollection: IRoleMySuffix[] = [...additionalRoleMySuffixes, ...roleCollection];
        jest.spyOn(roleService, 'addRoleMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        expect(roleService.query).toHaveBeenCalled();
        expect(roleService.addRoleMySuffixToCollectionIfMissing).toHaveBeenCalledWith(roleCollection, ...additionalRoleMySuffixes);
        expect(comp.rolesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactMySuffix query and add missing value', () => {
        const permission: IPermissionMySuffix = { id: 456 };
        const contact: IContactMySuffix = { id: 14692 };
        permission.contact = contact;

        const contactCollection: IContactMySuffix[] = [{ id: 64014 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactMySuffixes = [contact];
        const expectedCollection: IContactMySuffix[] = [...additionalContactMySuffixes, ...contactCollection];
        jest.spyOn(contactService, 'addContactMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ permission });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          contactCollection,
          ...additionalContactMySuffixes
        );
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const permission: IPermissionMySuffix = { id: 456 };
        const role: IRoleMySuffix = { id: 96832 };
        permission.role = role;
        const contact: IContactMySuffix = { id: 23817 };
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
        const saveSubject = new Subject<HttpResponse<PermissionMySuffix>>();
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
        const saveSubject = new Subject<HttpResponse<PermissionMySuffix>>();
        const permission = new PermissionMySuffix();
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
        const saveSubject = new Subject<HttpResponse<PermissionMySuffix>>();
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
      describe('trackRoleMySuffixById', () => {
        it('Should return tracked RoleMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRoleMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactMySuffixById', () => {
        it('Should return tracked ContactMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
