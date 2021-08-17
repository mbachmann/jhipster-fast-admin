import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILayoutFa, LayoutFa } from '../layout-fa.model';
import { LayoutFaService } from '../service/layout-fa.service';

@Component({
  selector: 'fa-layout-fa-update',
  templateUrl: './layout-fa-update.component.html',
})
export class LayoutFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
  });

  constructor(protected layoutService: LayoutFaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILayoutFa>>): void {
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

  protected updateForm(layout: ILayoutFa): void {
    this.editForm.patchValue({
      id: layout.id,
      remoteId: layout.remoteId,
    });
  }

  protected createFromForm(): ILayoutFa {
    return {
      ...new LayoutFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
    };
  }
}
