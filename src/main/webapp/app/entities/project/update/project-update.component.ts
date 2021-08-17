import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProject, Project } from '../project.model';
import { ProjectService } from '../service/project.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

@Component({
  selector: 'fa-project-update',
  templateUrl: './project-update.component.html',
})
export class ProjectUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    number: [],
    contactName: [],
    name: [null, [Validators.required]],
    description: [],
    startDate: [null, [Validators.required]],
    hoursEstimated: [],
    hourlyRate: [],
    status: [null, [Validators.required]],
    contact: [],
  });

  constructor(
    protected projectService: ProjectService,
    protected contactService: ContactService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.updateForm(project);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const project = this.createFromForm();
    if (project.id !== undefined) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>): void {
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

  protected updateForm(project: IProject): void {
    this.editForm.patchValue({
      id: project.id,
      remoteId: project.remoteId,
      number: project.number,
      contactName: project.contactName,
      name: project.name,
      description: project.description,
      startDate: project.startDate,
      hoursEstimated: project.hoursEstimated,
      hourlyRate: project.hourlyRate,
      status: project.status,
      contact: project.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(this.contactsSharedCollection, project.contact);
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

  protected createFromForm(): IProject {
    return {
      ...new Project(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      number: this.editForm.get(['number'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      hoursEstimated: this.editForm.get(['hoursEstimated'])!.value,
      hourlyRate: this.editForm.get(['hourlyRate'])!.value,
      status: this.editForm.get(['status'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
