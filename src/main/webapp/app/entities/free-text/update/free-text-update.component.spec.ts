jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FreeTextService } from '../service/free-text.service';
import { IFreeText, FreeText } from '../free-text.model';

import { FreeTextUpdateComponent } from './free-text-update.component';

describe('Component Tests', () => {
  describe('FreeText Management Update Component', () => {
    let comp: FreeTextUpdateComponent;
    let fixture: ComponentFixture<FreeTextUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let freeTextService: FreeTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FreeTextUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FreeTextUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FreeTextUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      freeTextService = TestBed.inject(FreeTextService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const freeText: IFreeText = { id: 456 };

        activatedRoute.data = of({ freeText });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(freeText));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FreeText>>();
        const freeText = { id: 123 };
        jest.spyOn(freeTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ freeText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: freeText }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(freeTextService.update).toHaveBeenCalledWith(freeText);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FreeText>>();
        const freeText = new FreeText();
        jest.spyOn(freeTextService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ freeText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: freeText }));
        saveSubject.complete();

        // THEN
        expect(freeTextService.create).toHaveBeenCalledWith(freeText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FreeText>>();
        const freeText = { id: 123 };
        jest.spyOn(freeTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ freeText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(freeTextService.update).toHaveBeenCalledWith(freeText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
