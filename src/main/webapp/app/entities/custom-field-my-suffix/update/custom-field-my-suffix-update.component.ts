import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICustomFieldMySuffix, CustomFieldMySuffix } from '../custom-field-my-suffix.model';
import { CustomFieldMySuffixService } from '../service/custom-field-my-suffix.service';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { ContactMySuffixService } from 'app/entities/contact-my-suffix/service/contact-my-suffix.service';
import { IContactPersonMySuffix } from 'app/entities/contact-person-my-suffix/contact-person-my-suffix.model';
import { ContactPersonMySuffixService } from 'app/entities/contact-person-my-suffix/service/contact-person-my-suffix.service';

@Component({
  selector: 'jhl-custom-field-my-suffix-update',
  templateUrl: './custom-field-my-suffix-update.component.html',
})
export class CustomFieldMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactMySuffix[] = [];
  contactPeopleSharedCollection: IContactPersonMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    domainArea: [],
    key: [],
    name: [],
    value: [],
    contact: [],
    contactPerson: [],
  });

  constructor(
    protected customFieldService: CustomFieldMySuffixService,
    protected contactService: ContactMySuffixService,
    protected contactPersonService: ContactPersonMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customField }) => {
      this.updateForm(customField);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customField = this.createFromForm();
    if (customField.id !== undefined) {
      this.subscribeToSaveResponse(this.customFieldService.update(customField));
    } else {
      this.subscribeToSaveResponse(this.customFieldService.create(customField));
    }
  }

  trackContactMySuffixById(index: number, item: IContactMySuffix): number {
    return item.id!;
  }

  trackContactPersonMySuffixById(index: number, item: IContactPersonMySuffix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomFieldMySuffix>>): void {
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

  protected updateForm(customField: ICustomFieldMySuffix): void {
    this.editForm.patchValue({
      id: customField.id,
      domainArea: customField.domainArea,
      key: customField.key,
      name: customField.name,
      value: customField.value,
      contact: customField.contact,
      contactPerson: customField.contactPerson,
    });

    this.contactsSharedCollection = this.contactService.addContactMySuffixToCollectionIfMissing(
      this.contactsSharedCollection,
      customField.contact
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonMySuffixToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      customField.contactPerson
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

    this.contactPersonService
      .query()
      .pipe(map((res: HttpResponse<IContactPersonMySuffix[]>) => res.body ?? []))
      .pipe(
        map((contactPeople: IContactPersonMySuffix[]) =>
          this.contactPersonService.addContactPersonMySuffixToCollectionIfMissing(contactPeople, this.editForm.get('contactPerson')!.value)
        )
      )
      .subscribe((contactPeople: IContactPersonMySuffix[]) => (this.contactPeopleSharedCollection = contactPeople));
  }

  protected createFromForm(): ICustomFieldMySuffix {
    return {
      ...new CustomFieldMySuffix(),
      id: this.editForm.get(['id'])!.value,
      domainArea: this.editForm.get(['domainArea'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      value: this.editForm.get(['value'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      contactPerson: this.editForm.get(['contactPerson'])!.value,
    };
  }
}
