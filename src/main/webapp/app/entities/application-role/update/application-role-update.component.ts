import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IApplicationRole, ApplicationRole } from '../application-role.model';
import { ApplicationRoleService } from '../service/application-role.service';

@Component({
  selector: 'fa-application-role-update',
  templateUrl: './application-role-update.component.html',
})
export class ApplicationRoleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected applicationRoleService: ApplicationRoleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationRole }) => {
      this.updateForm(applicationRole);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationRole = this.createFromForm();
    if (applicationRole.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationRoleService.update(applicationRole));
    } else {
      this.subscribeToSaveResponse(this.applicationRoleService.create(applicationRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationRole>>): void {
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

  protected updateForm(applicationRole: IApplicationRole): void {
    this.editForm.patchValue({
      id: applicationRole.id,
      name: applicationRole.name,
    });
  }

  protected createFromForm(): IApplicationRole {
    return {
      ...new ApplicationRole(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
