jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IsrService } from '../service/isr.service';
import { IIsr, Isr } from '../isr.model';

import { IsrUpdateComponent } from './isr-update.component';

describe('Component Tests', () => {
  describe('Isr Management Update Component', () => {
    let comp: IsrUpdateComponent;
    let fixture: ComponentFixture<IsrUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let isrService: IsrService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IsrUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IsrUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IsrUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      isrService = TestBed.inject(IsrService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const isr: IIsr = { id: 456 };

        activatedRoute.data = of({ isr });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(isr));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Isr>>();
        const isr = { id: 123 };
        jest.spyOn(isrService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ isr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: isr }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(isrService.update).toHaveBeenCalledWith(isr);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Isr>>();
        const isr = new Isr();
        jest.spyOn(isrService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ isr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: isr }));
        saveSubject.complete();

        // THEN
        expect(isrService.create).toHaveBeenCalledWith(isr);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Isr>>();
        const isr = { id: 123 };
        jest.spyOn(isrService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ isr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(isrService.update).toHaveBeenCalledWith(isr);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
