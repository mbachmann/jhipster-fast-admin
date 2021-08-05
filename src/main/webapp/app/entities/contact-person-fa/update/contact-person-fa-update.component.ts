import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContactPersonFa, ContactPersonFa } from '../contact-person-fa.model';
import { ContactPersonFaService } from '../service/contact-person-fa.service';

@Component({
  selector: 'fa-contact-person-fa-update',
  templateUrl: './contact-person-fa-update.component.html',
})
export class ContactPersonFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    defaultPerson: [],
    name: [],
    surname: [],
    gender: [],
    email: [],
    phone: [],
    department: [],
    salutation: [],
    showTitle: [],
    showDepartment: [],
    wantsNewsletter: [],
  });

  constructor(
    protected contactPersonService: ContactPersonFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactPerson }) => {
      this.updateForm(contactPerson);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactPerson = this.createFromForm();
    if (contactPerson.id !== undefined) {
      this.subscribeToSaveResponse(this.contactPersonService.update(contactPerson));
    } else {
      this.subscribeToSaveResponse(this.contactPersonService.create(contactPerson));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactPersonFa>>): void {
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

  protected updateForm(contactPerson: IContactPersonFa): void {
    this.editForm.patchValue({
      id: contactPerson.id,
      remoteId: contactPerson.remoteId,
      defaultPerson: contactPerson.defaultPerson,
      name: contactPerson.name,
      surname: contactPerson.surname,
      gender: contactPerson.gender,
      email: contactPerson.email,
      phone: contactPerson.phone,
      department: contactPerson.department,
      salutation: contactPerson.salutation,
      showTitle: contactPerson.showTitle,
      showDepartment: contactPerson.showDepartment,
      wantsNewsletter: contactPerson.wantsNewsletter,
    });
  }

  protected createFromForm(): IContactPersonFa {
    return {
      ...new ContactPersonFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      defaultPerson: this.editForm.get(['defaultPerson'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      department: this.editForm.get(['department'])!.value,
      salutation: this.editForm.get(['salutation'])!.value,
      showTitle: this.editForm.get(['showTitle'])!.value,
      showDepartment: this.editForm.get(['showDepartment'])!.value,
      wantsNewsletter: this.editForm.get(['wantsNewsletter'])!.value,
    };
  }
}
