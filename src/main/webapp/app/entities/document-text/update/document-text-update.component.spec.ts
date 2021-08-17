jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentTextService } from '../service/document-text.service';
import { IDocumentText, DocumentText } from '../document-text.model';

import { DocumentTextUpdateComponent } from './document-text-update.component';

describe('Component Tests', () => {
  describe('DocumentText Management Update Component', () => {
    let comp: DocumentTextUpdateComponent;
    let fixture: ComponentFixture<DocumentTextUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentTextService: DocumentTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentTextUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentTextUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentTextUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentTextService = TestBed.inject(DocumentTextService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const documentText: IDocumentText = { id: 456 };

        activatedRoute.data = of({ documentText });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documentText));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentText>>();
        const documentText = { id: 123 };
        jest.spyOn(documentTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentText }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentTextService.update).toHaveBeenCalledWith(documentText);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentText>>();
        const documentText = new DocumentText();
        jest.spyOn(documentTextService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentText }));
        saveSubject.complete();

        // THEN
        expect(documentTextService.create).toHaveBeenCalledWith(documentText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentText>>();
        const documentText = { id: 123 };
        jest.spyOn(documentTextService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentText });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentTextService.update).toHaveBeenCalledWith(documentText);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
