import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactAccountFa, ContactAccountFa } from '../contact-account-fa.model';
import { ContactAccountFaService } from '../service/contact-account-fa.service';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

@Component({
  selector: 'fa-contact-account-fa-update',
  templateUrl: './contact-account-fa-update.component.html',
})
export class ContactAccountFaUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactFa[] = [];

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
    protected contactAccountService: ContactAccountFaService,
    protected contactService: ContactFaService,
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

  trackContactFaById(index: number, item: IContactFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactAccountFa>>): void {
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

  protected updateForm(contactAccount: IContactAccountFa): void {
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

    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(
      this.contactsSharedCollection,
      contactAccount.contact
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IContactAccountFa {
    return {
      ...new ContactAccountFa(),
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
