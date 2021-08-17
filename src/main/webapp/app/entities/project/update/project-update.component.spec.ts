jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProjectService } from '../service/project.service';
import { IProject, Project } from '../project.model';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

import { ProjectUpdateComponent } from './project-update.component';

describe('Component Tests', () => {
  describe('Project Management Update Component', () => {
    let comp: ProjectUpdateComponent;
    let fixture: ComponentFixture<ProjectUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let projectService: ProjectService;
    let contactService: ContactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProjectUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProjectUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjectUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      projectService = TestBed.inject(ProjectService);
      contactService = TestBed.inject(ContactService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const project: IProject = { id: 456 };
        const contact: IContact = { id: 27950 };
        project.contact = contact;

        const contactCollection: IContact[] = [{ id: 61537 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const project: IProject = { id: 456 };
        const contact: IContact = { id: 48507 };
        project.contact = contact;

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(project));
        expect(comp.contactsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Project>>();
        const project = { id: 123 };
        jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: project }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(projectService.update).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Project>>();
        const project = new Project();
        jest.spyOn(projectService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: project }));
        saveSubject.complete();

        // THEN
        expect(projectService.create).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Project>>();
        const project = { id: 123 };
        jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(projectService.update).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackContactById', () => {
        it('Should return tracked Contact primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
