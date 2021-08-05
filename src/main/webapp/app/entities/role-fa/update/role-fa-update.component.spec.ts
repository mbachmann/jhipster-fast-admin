jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RoleFaService } from '../service/role-fa.service';
import { IRoleFa, RoleFa } from '../role-fa.model';

import { RoleFaUpdateComponent } from './role-fa-update.component';

describe('Component Tests', () => {
  describe('RoleFa Management Update Component', () => {
    let comp: RoleFaUpdateComponent;
    let fixture: ComponentFixture<RoleFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let roleService: RoleFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoleFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RoleFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoleFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      roleService = TestBed.inject(RoleFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const role: IRoleFa = { id: 456 };

        activatedRoute.data = of({ role });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(role));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RoleFa>>();
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
        const saveSubject = new Subject<HttpResponse<RoleFa>>();
        const role = new RoleFa();
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
        const saveSubject = new Subject<HttpResponse<RoleFa>>();
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
