jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplicationRoleService } from '../service/application-role.service';
import { IApplicationRole, ApplicationRole } from '../application-role.model';

import { ApplicationRoleUpdateComponent } from './application-role-update.component';

describe('Component Tests', () => {
  describe('ApplicationRole Management Update Component', () => {
    let comp: ApplicationRoleUpdateComponent;
    let fixture: ComponentFixture<ApplicationRoleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let applicationRoleService: ApplicationRoleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApplicationRoleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApplicationRoleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationRoleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      applicationRoleService = TestBed.inject(ApplicationRoleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const applicationRole: IApplicationRole = { id: 456 };

        activatedRoute.data = of({ applicationRole });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(applicationRole));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ApplicationRole>>();
        const applicationRole = { id: 123 };
        jest.spyOn(applicationRoleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicationRole });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicationRole }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(applicationRoleService.update).toHaveBeenCalledWith(applicationRole);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ApplicationRole>>();
        const applicationRole = new ApplicationRole();
        jest.spyOn(applicationRoleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicationRole });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicationRole }));
        saveSubject.complete();

        // THEN
        expect(applicationRoleService.create).toHaveBeenCalledWith(applicationRole);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ApplicationRole>>();
        const applicationRole = { id: 123 };
        jest.spyOn(applicationRoleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicationRole });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(applicationRoleService.update).toHaveBeenCalledWith(applicationRole);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
