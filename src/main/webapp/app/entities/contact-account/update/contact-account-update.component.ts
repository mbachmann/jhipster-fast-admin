import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactAccount, ContactAccount } from '../contact-account.model';
import { ContactAccountService } from '../service/contact-account.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

@Component({
  selector: 'fa-contact-account-update',
  templateUrl: './contact-account-update.component.html',
})
export class ContactAccountUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    defaultAccount: [],
    type: [],
    number: [],
    bic: [],
    description: [],
    inactiv: [],
    contact: [],
  });

  constructor(
    protected contactAccountService: ContactAccountService,
    protected contactService: ContactService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactAccount }) => {
      this.updateForm(contactAccount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactAccount = this.createFromForm();
    if (contactAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.contactAccountService.update(contactAccount));
    } else {
      this.subscribeToSaveResponse(this.contactAccountService.create(contactAccount));
    }
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactAccount>>): void {
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

  protected updateForm(contactAccount: IContactAccount): void {
    this.editForm.patchValue({
      id: contactAccount.id,
      remoteId: contactAccount.remoteId,
      defaultAccount: contactAccount.defaultAccount,
      type: contactAccount.type,
      number: contactAccount.number,
      bic: contactAccount.bic,
      description: contactAccount.description,
      inactiv: contactAccount.inactiv,
      contact: contactAccount.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      contactAccount.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContact[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContact[]) => this.contactService.addContactToCollectionIfMissing(contacts, this.editForm.get('contact')!.value))
      )
      .subscribe((contacts: IContact[]) => (this.contactsSharedCollection = contacts));
  }

  protected createFromForm(): IContactAccount {
    return {
      ...new ContactAccount(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      defaultAccount: this.editForm.get(['defaultAccount'])!.value,
      type: this.editForm.get(['type'])!.value,
      number: this.editForm.get(['number'])!.value,
      bic: this.editForm.get(['bic'])!.value,
      description: this.editForm.get(['description'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
