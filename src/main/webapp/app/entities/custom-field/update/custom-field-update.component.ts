import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICustomField, CustomField } from '../custom-field.model';
import { CustomFieldService } from '../service/custom-field.service';

@Component({
  selector: 'fa-custom-field-update',
  templateUrl: './custom-field-update.component.html',
})
export class CustomFieldUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    domainArea: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    defaultValue: [],
  });

  constructor(protected customFieldService: CustomFieldService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customField }) => {
      this.updateForm(customField);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customField = this.createFromForm();
    if (customField.id !== undefined) {
      this.subscribeToSaveResponse(this.customFieldService.update(customField));
    } else {
      this.subscribeToSaveResponse(this.customFieldService.create(customField));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomField>>): void {
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

  protected updateForm(customField: ICustomField): void {
    this.editForm.patchValue({
      id: customField.id,
      domainArea: customField.domainArea,
      key: customField.key,
      name: customField.name,
      defaultValue: customField.defaultValue,
    });
  }

  protected createFromForm(): ICustomField {
    return {
      ...new CustomField(),
      id: this.editForm.get(['id'])!.value,
      domainArea: this.editForm.get(['domainArea'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      defaultValue: this.editForm.get(['defaultValue'])!.value,
    };
  }
}
