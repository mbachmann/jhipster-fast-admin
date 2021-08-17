jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResourcePermissionService } from '../service/resource-permission.service';
import { IResourcePermission, ResourcePermission } from '../resource-permission.model';
import { IApplicationRole } from 'app/entities/application-role/application-role.model';
import { ApplicationRoleService } from 'app/entities/application-role/service/application-role.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

import { ResourcePermissionUpdateComponent } from './resource-permission-update.component';

describe('Component Tests', () => {
  describe('ResourcePermission Management Update Component', () => {
    let comp: ResourcePermissionUpdateComponent;
    let fixture: ComponentFixture<ResourcePermissionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let resourcePermissionService: ResourcePermissionService;
    let applicationRoleService: ApplicationRoleService;
    let applicationUserService: ApplicationUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResourcePermissionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ResourcePermissionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResourcePermissionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      resourcePermissionService = TestBed.inject(ResourcePermissionService);
      applicationRoleService = TestBed.inject(ApplicationRoleService);
      applicationUserService = TestBed.inject(ApplicationUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApplicationRole query and add missing value', () => {
        const resourcePermission: IResourcePermission = { id: 456 };
        const role: IApplicationRole = { id: 62333 };
        resourcePermission.role = role;

        const applicationRoleCollection: IApplicationRole[] = [{ id: 82442 }];
        jest.spyOn(applicationRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationRoleCollection })));
        const additionalApplicationRoles = [role];
        const expectedCollection: IApplicationRole[] = [...additionalApplicationRoles, ...applicationRoleCollection];
        jest.spyOn(applicationRoleService, 'addApplicationRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ resourcePermission });
        comp.ngOnInit();

        expect(applicationRoleService.query).toHaveBeenCalled();
        expect(applicationRoleService.addApplicationRoleToCollectionIfMissing).toHaveBeenCalledWith(
          applicationRoleCollection,
          ...additionalApplicationRoles
        );
        expect(comp.applicationRolesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ApplicationUser query and add missing value', () => {
        const resourcePermission: IResourcePermission = { id: 456 };
        const applicationUser: IApplicationUser = { id: 67836 };
        resourcePermission.applicationUser = applicationUser;

        const applicationUserCollection: IApplicationUser[] = [{ id: 79935 }];
        jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
        const additionalApplicationUsers = [applicationUser];
        const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
        jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ resourcePermission });
        comp.ngOnInit();

        expect(applicationUserService.query).toHaveBeenCalled();
        expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
          applicationUserCollection,
          ...additionalApplicationUsers
        );
        expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const resourcePermission: IResourcePermission = { id: 456 };
        const role: IApplicationRole = { id: 25804 };
        resourcePermission.role = role;
        const applicationUser: IApplicationUser = { id: 30236 };
        resourcePermission.applicationUser = applicationUser;

        activatedRoute.data = of({ resourcePermission });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(resourcePermission));
        expect(comp.applicationRolesSharedCollection).toContain(role);
        expect(comp.applicationUsersSharedCollection).toContain(applicationUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ResourcePermission>>();
        const resourcePermission = { id: 123 };
        jest.spyOn(resourcePermissionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ resourcePermission });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resourcePermission }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(resourcePermissionService.update).toHaveBeenCalledWith(resourcePermission);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ResourcePermission>>();
        const resourcePermission = new ResourcePermission();
        jest.spyOn(resourcePermissionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ resourcePermission });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resourcePermission }));
        saveSubject.complete();

        // THEN
        expect(resourcePermissionService.create).toHaveBeenCalledWith(resourcePermission);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ResourcePermission>>();
        const resourcePermission = { id: 123 };
        jest.spyOn(resourcePermissionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ resourcePermission });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(resourcePermissionService.update).toHaveBeenCalledWith(resourcePermission);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApplicationRoleById', () => {
        it('Should return tracked ApplicationRole primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplicationRoleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackApplicationUserById', () => {
        it('Should return tracked ApplicationUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplicationUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
