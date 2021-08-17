import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPermission, Permission } from '../permission.model';
import { PermissionService } from '../service/permission.service';

@Component({
  selector: 'fa-permission-update',
  templateUrl: './permission-update.component.html',
})
export class PermissionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected permissionService: PermissionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permission }) => {
      this.updateForm(permission);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permission = this.createFromForm();
    if (permission.id !== undefined) {
      this.subscribeToSaveResponse(this.permissionService.update(permission));
    } else {
      this.subscribeToSaveResponse(this.permissionService.create(permission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermission>>): void {
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

  protected updateForm(permission: IPermission): void {
    this.editForm.patchValue({
      id: permission.id,
      name: permission.name,
    });
  }

  protected createFromForm(): IPermission {
    return {
      ...new Permission(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
