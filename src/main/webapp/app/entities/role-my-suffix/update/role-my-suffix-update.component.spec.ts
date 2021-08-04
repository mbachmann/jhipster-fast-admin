jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RoleMySuffixService } from '../service/role-my-suffix.service';
import { IRoleMySuffix, RoleMySuffix } from '../role-my-suffix.model';

import { RoleMySuffixUpdateComponent } from './role-my-suffix-update.component';

describe('Component Tests', () => {
  describe('RoleMySuffix Management Update Component', () => {
    let comp: RoleMySuffixUpdateComponent;
    let fixture: ComponentFixture<RoleMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let roleService: RoleMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoleMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RoleMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoleMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      roleService = TestBed.inject(RoleMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const role: IRoleMySuffix = { id: 456 };

        activatedRoute.data = of({ role });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(role));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RoleMySuffix>>();
        const role = { id: 123 };
        jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ role });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: role }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(roleService.update).toHaveBeenCalledWith(role);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RoleMySuffix>>();
        const role = new RoleMySuffix();
        jest.spyOn(roleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ role });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: role }));
        saveSubject.complete();

        // THEN
        expect(roleService.create).toHaveBeenCalledWith(role);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RoleMySuffix>>();
        const role = { id: 123 };
        jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ role });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(roleService.update).toHaveBeenCalledWith(role);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
