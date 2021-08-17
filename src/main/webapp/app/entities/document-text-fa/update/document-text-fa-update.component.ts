import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDocumentTextFa, DocumentTextFa } from '../document-text-fa.model';
import { DocumentTextFaService } from '../service/document-text-fa.service';

@Component({
  selector: 'fa-document-text-fa-update',
  templateUrl: './document-text-fa-update.component.html',
})
export class DocumentTextFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    defaultText: [],
    text: [],
    language: [],
    usage: [],
    status: [],
    type: [],
    documentType: [],
  });

  constructor(protected documentTextService: DocumentTextFaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentText }) => {
      this.updateForm(documentText);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentText = this.createFromForm();
    if (documentText.id !== undefined) {
      this.subscribeToSaveResponse(this.documentTextService.update(documentText));
    } else {
      this.subscribeToSaveResponse(this.documentTextService.create(documentText));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentTextFa>>): void {
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

  protected updateForm(documentText: IDocumentTextFa): void {
    this.editForm.patchValue({
      id: documentText.id,
      defaultText: documentText.defaultText,
      text: documentText.text,
      language: documentText.language,
      usage: documentText.usage,
      status: documentText.status,
      type: documentText.type,
      documentType: documentText.documentType,
    });
  }

  protected createFromForm(): IDocumentTextFa {
    return {
      ...new DocumentTextFa(),
      id: this.editForm.get(['id'])!.value,
      defaultText: this.editForm.get(['defaultText'])!.value,
      text: this.editForm.get(['text'])!.value,
      language: this.editForm.get(['language'])!.value,
      usage: this.editForm.get(['usage'])!.value,
      status: this.editForm.get(['status'])!.value,
      type: this.editForm.get(['type'])!.value,
      documentType: this.editForm.get(['documentType'])!.value,
    };
  }
}
