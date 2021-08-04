import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactGroupMySuffix, ContactGroupMySuffix } from '../contact-group-my-suffix.model';
import { ContactGroupMySuffixService } from '../service/contact-group-my-suffix.service';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { ContactMySuffixService } from 'app/entities/contact-my-suffix/service/contact-my-suffix.service';

@Component({
  selector: 'jhl-contact-group-my-suffix-update',
  templateUrl: './contact-group-my-suffix-update.component.html',
})
export class ContactGroupMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    contact: [],
  });

  constructor(
    protected contactGroupService: ContactGroupMySuffixService,
    protected contactService: ContactMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactGroup }) => {
      this.updateForm(contactGroup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactGroup = this.createFromForm();
    if (contactGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.contactGroupService.update(contactGroup));
    } else {
      this.subscribeToSaveResponse(this.contactGroupService.create(contactGroup));
    }
  }

  trackContactMySuffixById(index: number, item: IContactMySuffix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactGroupMySuffix>>): void {
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

  protected updateForm(contactGroup: IContactGroupMySuffix): void {
    this.editForm.patchValue({
      id: contactGroup.id,
      name: contactGroup.name,
      contact: contactGroup.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactMySuffixToCollectionIfMissing(
      this.contactsSharedCollection,
      contactGroup.contact
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IContactGroupMySuffix {
    return {
      ...new ContactGroupMySuffix(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
