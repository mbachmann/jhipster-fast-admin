jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DocumentInvoiceWorkflowFaService } from '../service/document-invoice-workflow-fa.service';

import { DocumentInvoiceWorkflowFaDeleteDialogComponent } from './document-invoice-workflow-fa-delete-dialog.component';

describe('Component Tests', () => {
  describe('DocumentInvoiceWorkflowFa Management Delete Component', () => {
    let comp: DocumentInvoiceWorkflowFaDeleteDialogComponent;
    let fixture: ComponentFixture<DocumentInvoiceWorkflowFaDeleteDialogComponent>;
    let service: DocumentInvoiceWorkflowFaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentInvoiceWorkflowFaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(DocumentInvoiceWorkflowFaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentInvoiceWorkflowFaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentInvoiceWorkflowFaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
