import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LayoutDetailComponent } from './layout-detail.component';

describe('Component Tests', () => {
  describe('Layout Management Detail Component', () => {
    let comp: LayoutDetailComponent;
    let fixture: ComponentFixture<LayoutDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LayoutDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ layout: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LayoutDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LayoutDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load layout on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.layout).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
