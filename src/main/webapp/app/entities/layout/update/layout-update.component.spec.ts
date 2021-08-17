jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LayoutService } from '../service/layout.service';
import { ILayout, Layout } from '../layout.model';

import { LayoutUpdateComponent } from './layout-update.component';

describe('Component Tests', () => {
  describe('Layout Management Update Component', () => {
    let comp: LayoutUpdateComponent;
    let fixture: ComponentFixture<LayoutUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let layoutService: LayoutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LayoutUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LayoutUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LayoutUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      layoutService = TestBed.inject(LayoutService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const layout: ILayout = { id: 456 };

        activatedRoute.data = of({ layout });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(layout));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Layout>>();
        const layout = { id: 123 };
        jest.spyOn(layoutService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ layout });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: layout }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(layoutService.update).toHaveBeenCalledWith(layout);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Layout>>();
        const layout = new Layout();
        jest.spyOn(layoutService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ layout });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: layout }));
        saveSubject.complete();

        // THEN
        expect(layoutService.create).toHaveBeenCalledWith(layout);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Layout>>();
        const layout = { id: 123 };
        jest.spyOn(layoutService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ layout });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(layoutService.update).toHaveBeenCalledWith(layout);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});