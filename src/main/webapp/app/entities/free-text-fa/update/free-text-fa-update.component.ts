import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFreeTextFa, FreeTextFa } from '../free-text-fa.model';
import { FreeTextFaService } from '../service/free-text-fa.service';

@Component({
  selector: 'fa-free-text-fa-update',
  templateUrl: './free-text-fa-update.component.html',
})
export class FreeTextFaUpdateComponent implements OnInit {
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

  constructor(protected freeTextService: FreeTextFaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFreeTextFa>>): void {
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

  protected updateForm(freeText: IFreeTextFa): void {
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

  protected createFromForm(): IFreeTextFa {
    return {
      ...new FreeTextFa(),
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
