import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPermissionMySuffix, PermissionMySuffix } from '../permission-my-suffix.model';
import { PermissionMySuffixService } from '../service/permission-my-suffix.service';
import { IRoleMySuffix } from 'app/entities/role-my-suffix/role-my-suffix.model';
import { RoleMySuffixService } from 'app/entities/role-my-suffix/service/role-my-suffix.service';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { ContactMySuffixService } from 'app/entities/contact-my-suffix/service/contact-my-suffix.service';

@Component({
  selector: 'jhl-permission-my-suffix-update',
  templateUrl: './permission-my-suffix-update.component.html',
})
export class PermissionMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  rolesSharedCollection: IRoleMySuffix[] = [];
  contactsSharedCollection: IContactMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    add: [null, [Validators.required]],
    edit: [null, [Validators.required]],
    manage: [null, [Validators.required]],
    domainArea: [null, [Validators.required]],
    role: [],
    contact: [],
  });

  constructor(
    protected permissionService: PermissionMySuffixService,
    protected roleService: RoleMySuffixService,
    protected contactService: ContactMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permission }) => {
      this.updateForm(permission);

      this.loadRelationshipsOptions();
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

  trackRoleMySuffixById(index: number, item: IRoleMySuffix): number {
    return item.id!;
  }

  trackContactMySuffixById(index: number, item: IContactMySuffix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermissionMySuffix>>): void {
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

  protected updateForm(permission: IPermissionMySuffix): void {
    this.editForm.patchValue({
      id: permission.id,
      add: permission.add,
      edit: permission.edit,
      manage: permission.manage,
      domainArea: permission.domainArea,
      role: permission.role,
      contact: permission.contact,
    });

    this.rolesSharedCollection = this.roleService.addRoleMySuffixToCollectionIfMissing(this.rolesSharedCollection, permission.role);
    this.contactsSharedCollection = this.contactService.addContactMySuffixToCollectionIfMissing(
      this.contactsSharedCollection,
      permission.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRoleMySuffix[]>) => res.body ?? []))
      .pipe(map((roles: IRoleMySuffix[]) => this.roleService.addRoleMySuffixToCollectionIfMissing(roles, this.editForm.get('role')!.value)))
      .subscribe((roles: IRoleMySuffix[]) => (this.rolesSharedCollection = roles));

    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContactMySuffix[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContactMySuffix[]) =>
          this.contactService.addContactMySuffixToCollectionIfMissing(contacts, this.editForm.get('contact')!.value)
        )
      )
      .subscribe((contacts: IContactMySuffix[]) => (this.contactsSharedCollection = contacts));
  }

  protected createFromForm(): IPermissionMySuffix {
    return {
      ...new PermissionMySuffix(),
      id: this.editForm.get(['id'])!.value,
      add: this.editForm.get(['add'])!.value,
      edit: this.editForm.get(['edit'])!.value,
      manage: this.editForm.get(['manage'])!.value,
      domainArea: this.editForm.get(['domainArea'])!.value,
      role: this.editForm.get(['role'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
