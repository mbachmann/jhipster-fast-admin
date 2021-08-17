import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDocumentInvoiceWorkflow, DocumentInvoiceWorkflow } from '../document-invoice-workflow.model';
import { DocumentInvoiceWorkflowService } from '../service/document-invoice-workflow.service';

@Component({
  selector: 'fa-document-invoice-workflow-update',
  templateUrl: './document-invoice-workflow-update.component.html',
})
export class DocumentInvoiceWorkflowUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    active: [],
    status: [],
    overdueDays: [],
    action: [],
    contactEmail: [],
    speed: [],
  });

  constructor(
    protected documentInvoiceWorkflowService: DocumentInvoiceWorkflowService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentInvoiceWorkflow }) => {
      this.updateForm(documentInvoiceWorkflow);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentInvoiceWorkflow = this.createFromForm();
    if (documentInvoiceWorkflow.id !== undefined) {
      this.subscribeToSaveResponse(this.documentInvoiceWorkflowService.update(documentInvoiceWorkflow));
    } else {
      this.subscribeToSaveResponse(this.documentInvoiceWorkflowService.create(documentInvoiceWorkflow));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentInvoiceWorkflow>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(documentInvoiceWorkflow: IDocumentInvoiceWorkflow): void {
    this.editForm.patchValue({
      id: documentInvoiceWorkflow.id,
      active: documentInvoiceWorkflow.active,
      status: documentInvoiceWorkflow.status,
      overdueDays: documentInvoiceWorkflow.overdueDays,
      action: documentInvoiceWorkflow.action,
      contactEmail: documentInvoiceWorkflow.contactEmail,
      speed: documentInvoiceWorkflow.speed,
    });
  }

  protected createFromForm(): IDocumentInvoiceWorkflow {
    return {
      ...new DocumentInvoiceWorkflow(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      status: this.editForm.get(['status'])!.value,
      overdueDays: this.editForm.get(['overdueDays'])!.value,
      action: this.editForm.get(['action'])!.value,
      contactEmail: this.editForm.get(['contactEmail'])!.value,
      speed: this.editForm.get(['speed'])!.value,
    };
  }
}
