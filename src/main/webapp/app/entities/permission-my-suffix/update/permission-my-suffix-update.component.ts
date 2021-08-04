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
import { IContactAddressMySuffix } from 'app/entities/contact-address-my-suffix/contact-address-my-suffix.model';
import { ContactAddressMySuffixService } from 'app/entities/contact-address-my-suffix/service/contact-address-my-suffix.service';

@Component({
  selector: 'jhl-permission-my-suffix-update',
  templateUrl: './permission-my-suffix-update.component.html',
})
export class PermissionMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  rolesSharedCollection: IRoleMySuffix[] = [];
  contactsSharedCollection: IContactMySuffix[] = [];
  contactAddressesSharedCollection: IContactAddressMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    newAll: [null, [Validators.required]],
    editOwn: [null, [Validators.required]],
    editAll: [null, [Validators.required]],
    viewOwn: [null, [Validators.required]],
    viewAll: [null, [Validators.required]],
    manageOwn: [null, [Validators.required]],
    manageAll: [null, [Validators.required]],
    domainResource: [null, [Validators.required]],
    role: [],
    contact: [],
    contactAddress: [],
  });

  constructor(
    protected permissionService: PermissionMySuffixService,
    protected roleService: RoleMySuffixService,
    protected contactService: ContactMySuffixService,
    protected contactAddressService: ContactAddressMySuffixService,
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

  trackContactAddressMySuffixById(index: number, item: IContactAddressMySuffix): number {
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
      newAll: permission.newAll,
      editOwn: permission.editOwn,
      editAll: permission.editAll,
      viewOwn: permission.viewOwn,
      viewAll: permission.viewAll,
      manageOwn: permission.manageOwn,
      manageAll: permission.manageAll,
      domainResource: permission.domainResource,
      role: permission.role,
      contact: permission.contact,
      contactAddress: permission.contactAddress,
    });

    this.rolesSharedCollection = this.roleService.addRoleMySuffixToCollectionIfMissing(this.rolesSharedCollection, permission.role);
    this.contactsSharedCollection = this.contactService.addContactMySuffixToCollectionIfMissing(
      this.contactsSharedCollection,
      permission.contact
    );
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressMySuffixToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      permission.contactAddress
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

    this.contactAddressService
      .query()
      .pipe(map((res: HttpResponse<IContactAddressMySuffix[]>) => res.body ?? []))
      .pipe(
        map((contactAddresses: IContactAddressMySuffix[]) =>
          this.contactAddressService.addContactAddressMySuffixToCollectionIfMissing(
            contactAddresses,
            this.editForm.get('contactAddress')!.value
          )
        )
      )
      .subscribe((contactAddresses: IContactAddressMySuffix[]) => (this.contactAddressesSharedCollection = contactAddresses));
  }

  protected createFromForm(): IPermissionMySuffix {
    return {
      ...new PermissionMySuffix(),
      id: this.editForm.get(['id'])!.value,
      newAll: this.editForm.get(['newAll'])!.value,
      editOwn: this.editForm.get(['editOwn'])!.value,
      editAll: this.editForm.get(['editAll'])!.value,
      viewOwn: this.editForm.get(['viewOwn'])!.value,
      viewAll: this.editForm.get(['viewAll'])!.value,
      manageOwn: this.editForm.get(['manageOwn'])!.value,
      manageAll: this.editForm.get(['manageAll'])!.value,
      domainResource: this.editForm.get(['domainResource'])!.value,
      role: this.editForm.get(['role'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      contactAddress: this.editForm.get(['contactAddress'])!.value,
    };
  }
}
