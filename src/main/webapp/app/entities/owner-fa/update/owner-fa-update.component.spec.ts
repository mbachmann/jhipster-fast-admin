jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OwnerFaService } from '../service/owner-fa.service';
import { IOwnerFa, OwnerFa } from '../owner-fa.model';

import { OwnerFaUpdateComponent } from './owner-fa-update.component';

describe('Component Tests', () => {
  describe('OwnerFa Management Update Component', () => {
    let comp: OwnerFaUpdateComponent;
    let fixture: ComponentFixture<OwnerFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ownerService: OwnerFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OwnerFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OwnerFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OwnerFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ownerService = TestBed.inject(OwnerFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const owner: IOwnerFa = { id: 456 };

        activatedRoute.data = of({ owner });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(owner));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OwnerFa>>();
        const owner = { id: 123 };
        jest.spyOn(ownerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ owner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: owner }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ownerService.update).toHaveBeenCalledWith(owner);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OwnerFa>>();
        const owner = new OwnerFa();
        jest.spyOn(ownerService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ owner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: owner }));
        saveSubject.complete();

        // THEN
        expect(ownerService.create).toHaveBeenCalledWith(owner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OwnerFa>>();
        const owner = { id: 123 };
        jest.spyOn(ownerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ owner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ownerService.update).toHaveBeenCalledWith(owner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
