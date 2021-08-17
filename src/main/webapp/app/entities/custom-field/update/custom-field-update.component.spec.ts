jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomFieldService } from '../service/custom-field.service';
import { ICustomField, CustomField } from '../custom-field.model';

import { CustomFieldUpdateComponent } from './custom-field-update.component';

describe('Component Tests', () => {
  describe('CustomField Management Update Component', () => {
    let comp: CustomFieldUpdateComponent;
    let fixture: ComponentFixture<CustomFieldUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let customFieldService: CustomFieldService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomFieldUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CustomFieldUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomFieldUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      customFieldService = TestBed.inject(CustomFieldService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const customField: ICustomField = { id: 456 };

        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(customField));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomField>>();
        const customField = { id: 123 };
        jest.spyOn(customFieldService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customField }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(customFieldService.update).toHaveBeenCalledWith(customField);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomField>>();
        const customField = new CustomField();
        jest.spyOn(customFieldService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customField }));
        saveSubject.complete();

        // THEN
        expect(customFieldService.create).toHaveBeenCalledWith(customField);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomField>>();
        const customField = { id: 123 };
        jest.spyOn(customFieldService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(customFieldService.update).toHaveBeenCalledWith(customField);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
