jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoleMySuffixService } from '../service/role-my-suffix.service';

import { RoleMySuffixComponent } from './role-my-suffix.component';

describe('Component Tests', () => {
  describe('RoleMySuffix Management Component', () => {
    let comp: RoleMySuffixComponent;
    let fixture: ComponentFixture<RoleMySuffixComponent>;
    let service: RoleMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoleMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(RoleMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoleMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RoleMySuffixService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.roles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
