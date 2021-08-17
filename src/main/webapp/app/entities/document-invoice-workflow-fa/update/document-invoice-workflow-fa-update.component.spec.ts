jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentInvoiceWorkflowFaService } from '../service/document-invoice-workflow-fa.service';
import { IDocumentInvoiceWorkflowFa, DocumentInvoiceWorkflowFa } from '../document-invoice-workflow-fa.model';

import { DocumentInvoiceWorkflowFaUpdateComponent } from './document-invoice-workflow-fa-update.component';

describe('Component Tests', () => {
  describe('DocumentInvoiceWorkflowFa Management Update Component', () => {
    let comp: DocumentInvoiceWorkflowFaUpdateComponent;
    let fixture: ComponentFixture<DocumentInvoiceWorkflowFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentInvoiceWorkflowService: DocumentInvoiceWorkflowFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentInvoiceWorkflowFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentInvoiceWorkflowFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentInvoiceWorkflowFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentInvoiceWorkflowService = TestBed.inject(DocumentInvoiceWorkflowFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const documentInvoiceWorkflow: IDocumentInvoiceWorkflowFa = { id: 456 };

        activatedRoute.data = of({ documentInvoiceWorkflow });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documentInvoiceWorkflow));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentInvoiceWorkflowFa>>();
        const documentInvoiceWorkflow = { id: 123 };
        jest.spyOn(documentInvoiceWorkflowService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentInvoiceWorkflow });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentInvoiceWorkflow }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentInvoiceWorkflowService.update).toHaveBeenCalledWith(documentInvoiceWorkflow);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentInvoiceWorkflowFa>>();
        const documentInvoiceWorkflow = new DocumentInvoiceWorkflowFa();
        jest.spyOn(documentInvoiceWorkflowService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentInvoiceWorkflow });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentInvoiceWorkflow }));
        saveSubject.complete();

        // THEN
        expect(documentInvoiceWorkflowService.create).toHaveBeenCalledWith(documentInvoiceWorkflow);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentInvoiceWorkflowFa>>();
        const documentInvoiceWorkflow = { id: 123 };
        jest.spyOn(documentInvoiceWorkflowService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentInvoiceWorkflow });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentInvoiceWorkflowService.update).toHaveBeenCalledWith(documentInvoiceWorkflow);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
