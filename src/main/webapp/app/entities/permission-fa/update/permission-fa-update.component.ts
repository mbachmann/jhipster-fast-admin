import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPermissionFa, PermissionFa } from '../permission-fa.model';
import { PermissionFaService } from '../service/permission-fa.service';
import { IRoleFa } from 'app/entities/role-fa/role-fa.model';
import { RoleFaService } from 'app/entities/role-fa/service/role-fa.service';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

@Component({
  selector: 'fa-permission-fa-update',
  templateUrl: './permission-fa-update.component.html',
})
export class PermissionFaUpdateComponent implements OnInit {
  isSaving = false;

  rolesSharedCollection: IRoleFa[] = [];
  contactsSharedCollection: IContactFa[] = [];

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
    protected permissionService: PermissionFaService,
    protected roleService: RoleFaService,
    protected contactService: ContactFaService,
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

  trackRoleFaById(index: number, item: IRoleFa): number {
    return item.id!;
  }

  trackContactFaById(index: number, item: IContactFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermissionFa>>): void {
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

  protected updateForm(permission: IPermissionFa): void {
    this.editForm.patchValue({
      id: permission.id,
      add: permission.add,
      edit: permission.edit,
      manage: permission.manage,
      domainArea: permission.domainArea,
      role: permission.role,
      contact: permission.contact,
    });

    this.rolesSharedCollection = this.roleService.addRoleFaToCollectionIfMissing(this.rolesSharedCollection, permission.role);
    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(
      this.contactsSharedCollection,
      permission.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRoleFa[]>) => res.body ?? []))
      .pipe(map((roles: IRoleFa[]) => this.roleService.addRoleFaToCollectionIfMissing(roles, this.editForm.get('role')!.value)))
      .subscribe((roles: IRoleFa[]) => (this.rolesSharedCollection = roles));

    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContactFa[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContactFa[]) =>
          this.contactService.addContactFaToCollectionIfMissing(contacts, this.editForm.get('contact')!.value)
        )
      )
      .subscribe((contacts: IContactFa[]) => (this.contactsSharedCollection = contacts));
  }

  protected createFromForm(): IPermissionFa {
    return {
      ...new PermissionFa(),
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
