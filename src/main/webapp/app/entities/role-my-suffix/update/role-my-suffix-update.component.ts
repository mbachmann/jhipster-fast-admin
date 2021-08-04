import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRoleMySuffix, RoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixService } from '../service/role-my-suffix.service';

@Component({
  selector: 'jhl-role-my-suffix-update',
  templateUrl: './role-my-suffix-update.component.html',
})
export class RoleMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected roleService: RoleMySuffixService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.updateForm(role);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const role = this.createFromForm();
    if (role.id !== undefined) {
      this.subscribeToSaveResponse(this.roleService.update(role));
    } else {
      this.subscribeToSaveResponse(this.roleService.create(role));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoleMySuffix>>): void {
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

  protected updateForm(role: IRoleMySuffix): void {
    this.editForm.patchValue({
      id: role.id,
      name: role.name,
    });
  }

  protected createFromForm(): IRoleMySuffix {
    return {
      ...new RoleMySuffix(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
