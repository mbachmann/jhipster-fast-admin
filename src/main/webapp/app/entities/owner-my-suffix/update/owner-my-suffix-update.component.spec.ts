jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OwnerMySuffixService } from '../service/owner-my-suffix.service';
import { IOwnerMySuffix, OwnerMySuffix } from '../owner-my-suffix.model';

import { OwnerMySuffixUpdateComponent } from './owner-my-suffix-update.component';

describe('Component Tests', () => {
  describe('OwnerMySuffix Management Update Component', () => {
    let comp: OwnerMySuffixUpdateComponent;
    let fixture: ComponentFixture<OwnerMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ownerService: OwnerMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OwnerMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OwnerMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OwnerMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ownerService = TestBed.inject(OwnerMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const owner: IOwnerMySuffix = { id: 456 };

        activatedRoute.data = of({ owner });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(owner));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OwnerMySuffix>>();
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
        const saveSubject = new Subject<HttpResponse<OwnerMySuffix>>();
        const owner = new OwnerMySuffix();
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
        const saveSubject = new Subject<HttpResponse<OwnerMySuffix>>();
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
