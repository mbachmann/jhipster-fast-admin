import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProjectFaDetailComponent } from './project-fa-detail.component';

describe('Component Tests', () => {
  describe('ProjectFa Management Detail Component', () => {
    let comp: ProjectFaDetailComponent;
    let fixture: ComponentFixture<ProjectFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProjectFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ project: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProjectFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProjectFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load project on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.project).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
