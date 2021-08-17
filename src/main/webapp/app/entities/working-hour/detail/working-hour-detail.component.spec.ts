import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkingHourDetailComponent } from './working-hour-detail.component';

describe('Component Tests', () => {
  describe('WorkingHour Management Detail Component', () => {
    let comp: WorkingHourDetailComponent;
    let fixture: ComponentFixture<WorkingHourDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WorkingHourDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ workingHour: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WorkingHourDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkingHourDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load workingHour on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workingHour).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
