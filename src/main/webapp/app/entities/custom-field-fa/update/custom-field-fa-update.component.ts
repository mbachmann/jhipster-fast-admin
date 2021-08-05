import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICustomFieldFa, CustomFieldFa } from '../custom-field-fa.model';
import { CustomFieldFaService } from '../service/custom-field-fa.service';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ContactPersonFaService } from 'app/entities/contact-person-fa/service/contact-person-fa.service';

@Component({
  selector: 'fa-custom-field-fa-update',
  templateUrl: './custom-field-fa-update.component.html',
})
export class CustomFieldFaUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactFa[] = [];
  contactPeopleSharedCollection: IContactPersonFa[] = [];

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
    protected customFieldService: CustomFieldFaService,
    protected contactService: ContactFaService,
    protected contactPersonService: ContactPersonFaService,
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

  trackContactFaById(index: number, item: IContactFa): number {
    return item.id!;
  }

  trackContactPersonFaById(index: number, item: IContactPersonFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomFieldFa>>): void {
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

  protected updateForm(customField: ICustomFieldFa): void {
    this.editForm.patchValue({
      id: customField.id,
      domainArea: customField.domainArea,
      key: customField.key,
      name: customField.name,
      value: customField.value,
      contact: customField.contact,
      contactPerson: customField.contactPerson,
    });

    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(
      this.contactsSharedCollection,
      customField.contact
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonFaToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      customField.contactPerson
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

    this.contactPersonService
      .query()
      .pipe(map((res: HttpResponse<IContactPersonFa[]>) => res.body ?? []))
      .pipe(
        map((contactPeople: IContactPersonFa[]) =>
          this.contactPersonService.addContactPersonFaToCollectionIfMissing(contactPeople, this.editForm.get('contactPerson')!.value)
        )
      )
      .subscribe((contactPeople: IContactPersonFa[]) => (this.contactPeopleSharedCollection = contactPeople));
  }

  protected createFromForm(): ICustomFieldFa {
    return {
      ...new CustomFieldFa(),
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
