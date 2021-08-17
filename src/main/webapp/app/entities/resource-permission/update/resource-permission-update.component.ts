import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IResourcePermission, ResourcePermission } from '../resource-permission.model';
import { ResourcePermissionService } from '../service/resource-permission.service';
import { IApplicationRole } from 'app/entities/application-role/application-role.model';
import { ApplicationRoleService } from 'app/entities/application-role/service/application-role.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

@Component({
  selector: 'fa-resource-permission-update',
  templateUrl: './resource-permission-update.component.html',
})
export class ResourcePermissionUpdateComponent implements OnInit {
  isSaving = false;

  applicationRolesSharedCollection: IApplicationRole[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    add: [null, [Validators.required]],
    edit: [null, [Validators.required]],
    manage: [null, [Validators.required]],
    domainArea: [null, [Validators.required]],
    role: [],
    applicationUser: [],
  });

  constructor(
    protected resourcePermissionService: ResourcePermissionService,
    protected applicationRoleService: ApplicationRoleService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourcePermission }) => {
      this.updateForm(resourcePermission);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resourcePermission = this.createFromForm();
    if (resourcePermission.id !== undefined) {
      this.subscribeToSaveResponse(this.resourcePermissionService.update(resourcePermission));
    } else {
      this.subscribeToSaveResponse(this.resourcePermissionService.create(resourcePermission));
    }
  }

  trackApplicationRoleById(index: number, item: IApplicationRole): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourcePermission>>): void {
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

  protected updateForm(resourcePermission: IResourcePermission): void {
    this.editForm.patchValue({
      id: resourcePermission.id,
      add: resourcePermission.add,
      edit: resourcePermission.edit,
      manage: resourcePermission.manage,
      domainArea: resourcePermission.domainArea,
      role: resourcePermission.role,
      applicationUser: resourcePermission.applicationUser,
    });

    this.applicationRolesSharedCollection = this.applicationRoleService.addApplicationRoleToCollectionIfMissing(
      this.applicationRolesSharedCollection,
      resourcePermission.role
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      resourcePermission.applicationUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationRoleService
      .query()
      .pipe(map((res: HttpResponse<IApplicationRole[]>) => res.body ?? []))
      .pipe(
        map((applicationRoles: IApplicationRole[]) =>
          this.applicationRoleService.addApplicationRoleToCollectionIfMissing(applicationRoles, this.editForm.get('role')!.value)
        )
      )
      .subscribe((applicationRoles: IApplicationRole[]) => (this.applicationRolesSharedCollection = applicationRoles));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('applicationUser')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): IResourcePermission {
    return {
      ...new ResourcePermission(),
      id: this.editForm.get(['id'])!.value,
      add: this.editForm.get(['add'])!.value,
      edit: this.editForm.get(['edit'])!.value,
      manage: this.editForm.get(['manage'])!.value,
      domainArea: this.editForm.get(['domainArea'])!.value,
      role: this.editForm.get(['role'])!.value,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
    };
  }
}
