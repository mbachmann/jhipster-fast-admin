import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EffortDetailComponent } from './effort-detail.component';

describe('Component Tests', () => {
  describe('Effort Management Detail Component', () => {
    let comp: EffortDetailComponent;
    let fixture: ComponentFixture<EffortDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EffortDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ effort: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EffortDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EffortDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load effort on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.effort).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
