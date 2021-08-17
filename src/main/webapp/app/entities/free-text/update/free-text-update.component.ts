import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFreeText, FreeText } from '../free-text.model';
import { FreeTextService } from '../service/free-text.service';

@Component({
  selector: 'fa-free-text-update',
  templateUrl: './free-text-update.component.html',
})
export class FreeTextUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    text: [],
    fontSize: [],
    positionX: [],
    positionY: [],
    pageNo: [],
    language: [],
  });

  constructor(protected freeTextService: FreeTextService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ freeText }) => {
      this.updateForm(freeText);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const freeText = this.createFromForm();
    if (freeText.id !== undefined) {
      this.subscribeToSaveResponse(this.freeTextService.update(freeText));
    } else {
      this.subscribeToSaveResponse(this.freeTextService.create(freeText));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFreeText>>): void {
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

  protected updateForm(freeText: IFreeText): void {
    this.editForm.patchValue({
      id: freeText.id,
      text: freeText.text,
      fontSize: freeText.fontSize,
      positionX: freeText.positionX,
      positionY: freeText.positionY,
      pageNo: freeText.pageNo,
      language: freeText.language,
    });
  }

  protected createFromForm(): IFreeText {
    return {
      ...new FreeText(),
      id: this.editForm.get(['id'])!.value,
      text: this.editForm.get(['text'])!.value,
      fontSize: this.editForm.get(['fontSize'])!.value,
      positionX: this.editForm.get(['positionX'])!.value,
      positionY: this.editForm.get(['positionY'])!.value,
      pageNo: this.editForm.get(['pageNo'])!.value,
      language: this.editForm.get(['language'])!.value,
    };
  }
}
