import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILayout, Layout } from '../layout.model';
import { LayoutService } from '../service/layout.service';

@Component({
  selector: 'fa-layout-update',
  templateUrl: './layout-update.component.html',
})
export class LayoutUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
  });

  constructor(protected layoutService: LayoutService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layout }) => {
      this.updateForm(layout);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const layout = this.createFromForm();
    if (layout.id !== undefined) {
      this.subscribeToSaveResponse(this.layoutService.update(layout));
    } else {
      this.subscribeToSaveResponse(this.layoutService.create(layout));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILayout>>): void {
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

  protected updateForm(layout: ILayout): void {
    this.editForm.patchValue({
      id: layout.id,
      remoteId: layout.remoteId,
    });
  }

  protected createFromForm(): ILayout {
    return {
      ...new Layout(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
    };
  }
}
